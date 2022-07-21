package com.te6lim.ytcviewer.model

class SortType(
    val name: String, var isSelected: Boolean = false, val query: String, val isAsc: Boolean
) {
    companion object {
        val list = listOf(
            SortType("Name(ASC)", query = "name", isAsc = true, isSelected = true),
            SortType("Name(DESC)", query = "name", isAsc = false),
            SortType("Level/Rank(ASC)", query = "level", isAsc = true),
            SortType("Level/Rank(DESC)", query = "level", isAsc = false),
            SortType("ATK(ASC)", query = "atk", isAsc = true),
            SortType("ATK(DESC)", query = "atk", isAsc = false),
            SortType("DEF(ASC)", query = "def", isAsc = true),
            SortType("DEF(DESC)", query = "def", isAsc = false)
        )

        fun getItems(): List<SortType> {
            return list
        }

        val defaultSortType = list[0]
    }
}