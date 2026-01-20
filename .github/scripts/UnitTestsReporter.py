import glob
import os
from dataclasses import dataclass
from typing import List, Dict, Tuple
from xml.etree import ElementTree


@dataclass
class TestResult:
    name: str
    classname: str
    status: str  # 'PASS', 'FAIL', 'SKIP'
    time: float


class ModuleReport:
    def __init__(self, name: str):
        self.name = name
        # Group results by class name: { "ViewModelTest": [TestResult, ...] }
        self.class_results: Dict[str, List[TestResult]] = {}
        self.total_time = 0.0

    def add_test(self, result: TestResult):
        if result.classname not in self.class_results:
            self.class_results[result.classname] = []
        self.class_results[result.classname].append(result)
        self.total_time += result.time

    def get_tests_flatmap(self) -> List[TestResult]:
        return [test_result for list_results in self.class_results.values() for test_result in list_results]

    def get_stats(self) -> Tuple[int, int, int]:
        all_tests = self.get_tests_flatmap()
        passed = sum(1 for t in all_tests if t.status == 'PASS')
        failed = sum(1 for t in all_tests if t.status == 'FAIL')
        skipped = sum(1 for t in all_tests if t.status == 'SKIP')
        return passed, failed, skipped


class TestReporter:
    def __init__(self, paths: list[str]):
        self.paths = paths
        self.modules: Dict[str, ModuleReport] = {}

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
                self.modules[module_name] = ModuleReport(module_name)

            tree = ElementTree.parse(path)
            for testcase in tree.getroot().findall('testcase'):
                # Get raw class name without packages
                full_class = testcase.attrib.get('classname', 'Unknown')
                short_class = full_class.split('.')[-1]

                status = 'PASS'
                if testcase.find('failure') is not None:
                    status = 'FAIL'
                elif testcase.find('skipped') is not None:
                    status = 'SKIP'

                result = TestResult(
                    name=testcase.attrib.get('name', 'unknown'),
                    classname=short_class,
                    status=status,
                    time=float(testcase.attrib.get('time', 0))
                )
                self.modules[module_name].add_test(result)

    def generate_summary(self) -> str:
        parts: List[str] = list()
        total_p = 0
        total_f = 0
        total_s = 0
        total_t = 0.0

        # Collect general statistics
        for module in self.modules.values():
            passed, failed, skipped = module.get_stats()
            total_p += passed
            total_f += failed
            total_s += skipped
            total_t += module.total_time

        # Вивід у форматі GitHub Markdown
        parts.append(f"## 📊 Total Summary")
        parts.append(f"| ✅ Passed | ❌ Failed | 💤 Skipped | ⏱️ Time |")
        parts.append(f"| --- | --- | --- | --- |")
        parts.append(f"| {total_p} | **{total_f}** | {total_s} | {total_t:.2f}s |\n")

        parts.append(f"## 📦 Detailed Module Report")
        for mod_name, mod in sorted(self.modules.items()):
            passed, failed, skipped = mod.get_stats()
            icon = "❌" if failed > 0 else "✅"

            # Use <details> for succeed modules, to decrease report size
            is_open = "open" if failed > 0 else ""
            parts.append(f"<details {is_open}><summary>")
            parts.append(f"{icon} <b>Module: {mod_name}</b> ({passed} passed, {failed} failed)")
            parts.append(f"</summary>\n")

            for cls_name, tests in sorted(mod.class_results.items()):
                cls_failed = [t for t in tests if t.status == 'FAIL']
                if not cls_failed:
                    parts.append(f"- **{cls_name}**: ✅ {len(tests)} passed")
                else:
                    parts.append(f"- **{cls_name}**: ❌ {len(cls_failed)}/{len(tests)} failed")
                    for t in cls_failed:
                        parts.append(f"  - `❌ {t.name}`")
            parts.append(f"</details>\n")
        return "\n".join(parts)


if __name__ == "__main__":
    pure_kotlin_module_tests = '**/build/test-results/test/TEST-*.xml'
    android_module_tests = '**/build/test-results/testDebugUnitTest/TEST-*.xml'

    patterns = [
        pure_kotlin_module_tests,
        android_module_tests,
    ]

    found_files = list()
    for pattern in patterns:
        found_files.extend(glob.glob(pattern, recursive=True))

    unique_files = list(set(found_files))

    reporter = TestReporter(unique_files)
    reporter.parse()
    print(reporter.generate_summary())
