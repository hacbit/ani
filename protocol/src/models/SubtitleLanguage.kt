/*
 * Animation Garden App
 * Copyright (C) 2022  Him188
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.him188.animationgarden.shared.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(SubtitleLanguage.Serializer::class)
sealed class SubtitleLanguage(
    val id: String,
) {
    abstract fun matches(text: String): Boolean
    override fun toString(): String {
        return id
    }

    sealed class Chinese(id: String) : SubtitleLanguage(id)

    object ChineseSimplified : Chinese("CHS") {
        private val tokens =
            arrayOf(
                "简中",
                "GB",
                "GBK",
                "简体中文",
                "中文",
                "中字",
                "簡",
                "简",
                "CHS",
                "Zh-Hans",
                "Zh_Hans",
                "zh_cn",
                "SC"
            )

        override fun matches(text: String): Boolean {
            return tokens.any { text.contains(it, ignoreCase = true) }
        }
    }

    object ChineseTraditional : Chinese("CHT") {
        private val tokens = arrayOf("繁中", "BIG5", "BIG 5", "繁", "Chinese", "CHT", "TC")
        override fun matches(text: String): Boolean {
            return tokens.any { text.contains(it, ignoreCase = true) }
        }

    }

    object Japanese : SubtitleLanguage("JPN") {
        private val tokens = arrayOf("日", "Japanese", "JP")
        override fun matches(text: String): Boolean {
            return tokens.any { text.contains(it, ignoreCase = true) }
        }
    }

    object English : SubtitleLanguage("ENG") {
        private val tokens = arrayOf("英", "English")
        override fun matches(text: String): Boolean {
            return tokens.any { text.contains(it, ignoreCase = true) }
        }
    }

    object Other : SubtitleLanguage("Other") {
        override fun matches(text: String): Boolean {
            return true
        }
    }

    object ParseError : SubtitleLanguage("ERROR") {
        override fun matches(text: String): Boolean {
            return false
        }
    }

    internal object Serializer : KSerializer<SubtitleLanguage> {
        override val descriptor: SerialDescriptor = String.serializer().descriptor

        override fun deserialize(decoder: Decoder): SubtitleLanguage {
            return tryParse(String.serializer().deserialize(decoder)) ?: ParseError
        }

        override fun serialize(encoder: Encoder, value: SubtitleLanguage) {
            return String.serializer().serialize(encoder, value.id)
        }
    }


    companion object {
        val matchableEntries = arrayOf(ChineseSimplified, ChineseTraditional, Japanese, English)

        fun tryParse(value: String): SubtitleLanguage? {
            for (entry in matchableEntries) {
                if (entry.matches(value)) {
                    return entry
                }
            }
            return null
        }
    }
}