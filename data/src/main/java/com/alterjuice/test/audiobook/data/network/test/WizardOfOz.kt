package com.alterjuice.test.audiobook.data.network.test

import com.alterjuice.test.audiobook.data.network.model.BookDto
import com.alterjuice.test.audiobook.data.network.model.ChapterDto


val WizardOfOzData by lazy {
    // https://etc.usf.edu/lit2go/158/the-wonderful-wizard-of-oz/
    BookDto(
        id = "wizard-of-oz",
        title = "The Wonderful Wizard of Oz",
        language = "English",
        author = "L. Frank Baum",
        coverArtUrl = "https://d5d5yejrba9lo.cloudfront.net/keyart-jpeg/movies/media/browser/wizardofoz_v_dd_ka_tt_2000x3000_300dpi_en.jpg",
        description = "The Wonderful Wizard of Oz is a children’s book written in 1900 by L. Frank Baum and illustrated by W.W. Denslow. It was originally published by the George M. Hill company in Chicago, and has since been reprinted countless times, sometimes under the name The Wizard of Oz. The story chronicles the adventures of a girl named Dorothy in the land of Oz. It is one of the best-known stories in American popular culture and has been widely translated. Its initial success led to Baum’s writing and having published thirteen more Oz books.",
        chapters = listOf(
            ChapterDto(id = "intro", order = 0, title = "Introduction", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-001-introduction.2746.mp3"),
            ChapterDto(id = "ch1", order = 1, title = "Chapter 1: “The Cyclone”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-002-chapter-1-the-cyclone.2747.mp3"),
            ChapterDto(id = "ch2", order = 2, title = "Chapter 2: “The Council with the Munchkins”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-003-chapter-2-the-council-with-the-munchkins.2748.mp3"),
            ChapterDto(id = "ch3", order = 3, title = "Chapter 3: “How Dorothy Saved the Scarecrow”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-004-chapter-3-how-dorothy-saved-the-scarecrow.2749.mp3"),
            ChapterDto(id = "ch4", order = 4, title = "Chapter 4: “The Road through the Forest”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-006-chapter-5-the-rescue-of-the-tin-woodman.2751.mp3"),
            ChapterDto(id = "ch5", order = 5, title = "Chapter 5: “The Rescue of the Tin Woodman”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-006-chapter-5-the-rescue-of-the-tin-woodman.2751.mp3"),
            ChapterDto(id = "ch6", order = 6, title = "Chapter 6: “The Cowardly Lion”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-007-chapter-6-the-cowardly-lion.2752.mp3"),
            ChapterDto(id = "ch7", order = 7, title = "Chapter 7: “The Journey to the Great Oz”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-008-chapter-7-the-journey-to-the-great-oz.2753.mp3"),
            ChapterDto(id = "ch8", order = 8, title = "Chapter 8: “The Deadly Poppy Field”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-009-chapter-8-the-deadly-poppy-field.2754.mp3"),
            ChapterDto(id = "ch9", order = 9, title = "Chapter 9: “The Queen of the Field Mice”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-010-chapter-9-the-queen-of-the-field-mice.2755.mp3"),
            ChapterDto(id = "ch10", order = 10, title = "Chapter 10: “The Guardian of the Gate”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-011-chapter-10-the-guardian-of-the-gate.2756.mp3"),
            ChapterDto(id = "ch11", order = 11, title = "Chapter 11: “The Wonderful City of Oz”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-012-chapter-11-the-wonderful-city-of-oz.2757.mp3"),
            ChapterDto(id = "ch12", order = 12, title = "Chapter 12: “The Search for the Wicked Witch”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-013-chapter-12-the-search-for-the-wicked-witch.2758.mp3"),
            ChapterDto(id = "ch13", order = 13, title = "Chapter 13: “The Rescue”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-014-chapter-13-the-rescue.2759.mp3"),
            ChapterDto(id = "ch14", order = 14, title = "Chapter 14: “The Winged Monkeys”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-015-chapter-14-the-winged-monkeys.2760.mp3"),
            ChapterDto(id = "ch15", order = 15, title = "Chapter 15: “The Discovery of Oz, the Terrible”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-016-chapter-15-the-discovery-of-oz-the-terrible.2761.mp3"),
            ChapterDto(id = "ch16", order = 16, title = "Chapter 16: “The Magic Art of the Great Humbug”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-017-chapter-16-the-magic-art-of-the-great-humbug.2762.mp3"),
            ChapterDto(id = "ch17", order = 17, title = "Chapter 17: “How the Balloon Was Launched”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-018-chapter-17-how-the-balloon-was-launched.2763.mp3"),
            ChapterDto(id = "ch18", order = 18, title = "Chapter 18: “Away to the South”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-019-chapter-18-away-to-the-south.2764.mp3"),
            ChapterDto(id = "ch19", order = 19, title = "Chapter 19: “Attacked by the Fighting Trees”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-020-chapter-19-attacked-by-the-fighting-trees.2765.mp3"),
            ChapterDto(id = "ch20", order = 20, title = "Chapter 20: “The Dainty China Country”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-021-chapter-20-the-dainty-china-country.2766.mp3"),
            ChapterDto(id = "ch21", order = 21, title = "Chapter 21: “The Lion Becomes the King of Beasts”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-022-chapter-21-the-lion-becomes-the-king-of-beasts.2770.mp3"),
            ChapterDto(id = "ch22", order = 22, title = "Chapter 22: “The Country of the Quadlings”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-023-chapter-22-the-country-of-the-quadlings.2767.mp3"),
            ChapterDto(id = "ch23", order = 23, title = "Chapter 23: “Glinda the Good Witch Grants Dorothy’s Wish”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-024-chapter-23-glinda-the-good-witch-grants-dorothys-wish.2768.mp3"),
            ChapterDto(id = "ch24", order = 24, title = "Chapter 24: “Home Again”", audioUrl = "https://etc.usf.edu/lit2go/audio/mp3/the-wonderful-wizard-of-oz-025-chapter-24-home-again.2769.mp3"
            )
        )
    )
}