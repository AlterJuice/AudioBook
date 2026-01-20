import glob
import os
import sys
from dataclasses import dataclass
from typing import List, Dict, Tuple
from xml.etree import ElementTree


@dataclass
class LintIssue:
    id: str
    severity: str  # 'Error', 'Warning', 'Information'
    message: str
    file: str
    line: str


class ModuleLintReport:
    def __init__(self, name: str):
        self.name = name
        # Group by error type: { "HardcodedText": [LintIssue, ...] }
        self.issues_by_type: Dict[str, List[LintIssue]] = {}

    def add_issue(self, issue: LintIssue):
        if issue.id not in self.issues_by_type:
            self.issues_by_type[issue.id] = []
        self.issues_by_type[issue.id].append(issue)

    def get_stats(self) -> Tuple[int, int]:
        all_issues = [issue for list_issues in self.issues_by_type.values() for issue in list_issues]
        errors = sum(1 for i in all_issues if i.severity == 'Error' or i.severity == 'Fatal')
        warnings = sum(1 for i in all_issues if i.severity == 'Warning')
        return errors, warnings


class LintReporter:
    def __init__(self, paths: list[str]):
        self.paths = paths
        self.modules: Dict[str, ModuleLintReport] = {}

    def _extract_module_name(self, file_path: str) -> str:
        parts = file_path.split(os.sep)
        try:
            build_index = parts.index('build')
            return ":".join(parts[:build_index])
        except ValueError:
            return "root"

    def parse(self):
        for path in self.paths:
            module_name = self._extract_module_name(path)
            if module_name not in self.modules:
                self.modules[module_name] = ModuleLintReport(module_name)

            tree = ElementTree.parse(path)
            # In Android Lint XML every problem marked with <issue> tag
            for issue_node in tree.getroot().findall('issue'):
                location = issue_node.find('location')
                file_name = location.attrib.get('file', 'Unknown') if location is not None else "Unknown"
                line_num = location.attrib.get('line', '?') if location is not None else "?"

                issue = LintIssue(
                    id=issue_node.attrib.get('id', 'Unknown'),
                    severity=issue_node.attrib.get('severity', 'Unknown'),
                    message=issue_node.attrib.get('message', ''),
                    file=os.path.basename(file_name),
                    line=line_num
                )
                self.modules[module_name].add_issue(issue)

    def generate_summary(self) -> str:
        parts: List[str] = []
        total_e = 0
        total_w = 0

        # Calculate general statistics
        for module in self.modules.values():
            e, w = module.get_stats()
            total_e += e
            total_w += w

        parts.append(f"## 🔍 Lint Summary")
        parts.append(f"| ❌ Errors | ⚠️ Warnings |")
        parts.append(f"| --- | --- |")
        parts.append(f"| **{total_e}** | {total_w} |\n")

        parts.append(f"## 📦 Detailed Lint Report")

        sorted_modules = sorted(
            self.modules.items(),
            key=lambda x: (x[1].get_stats()[0] > 0),
            reverse=True
        )

        for mod_name, mod in sorted_modules:
            errors, warnings = mod.get_stats()
            if errors == 0 and warnings == 0:
                continue

            icon = "❌" if errors > 0 else "⚠️"
            is_open = "open" if errors > 0 else ""

            parts.append(f"<details {is_open}><summary>")
            parts.append(f"{icon} <b>Module: {mod_name}</b> ({errors} errors, {warnings} warnings)")
            parts.append(f"</summary>\n")

            # Sort type of errors inside module:
            # severity 'Error' or 'Fatal' are coming first
            sorted_issue_types = sorted(
                mod.issues_by_type.items(),
                key=lambda x: (x[1][0].severity in ['Error', 'Fatal']),
                reverse=True
            )

            for issue_id, issues in sorted_issue_types:
                severity_icon = "❌" if issues[0].severity in ['Error', 'Fatal'] else "⚠️"
                parts.append(f"- **{issue_id}** ({severity_icon} {len(issues)})")
                for i in issues:
                    # Add relative link to a file if it is possible
                    parts.append(f"  - `{i.file}:{i.line}`: {i.message}")

            parts.append(f"</details>\n")

        if not self.modules:
            parts.append("✅ No lint reports found.")

        return "\n".join(parts)


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("❌ Помилка: Не вказано шлях до XML файлу.")
        print("Використання: python3 LinterReporter.py <шлях_до_xml>")
        sys.exit(1)

    file_path = sys.argv[1]

    if not os.path.exists(file_path):
        print(f"⚠️ Файл не знайдено: {file_path}")
        sys.exit(0)

    reporter = LintReporter([file_path])
    reporter.parse()
    print(reporter.generate_summary())