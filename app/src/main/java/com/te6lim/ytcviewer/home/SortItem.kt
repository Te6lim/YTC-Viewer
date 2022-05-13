package com.te6lim.ytcviewer.home

class SortItem(
    val name: String, var isSelected: Boolean = false, val query: String, val isAsc: Boolean
) {
    companion object {
        val list = listOf(
            SortItem("Name(ASC)", query = "name", isAsc = true, isSelected = true),
            SortItem("Name(DESC)", query = "name", isAsc = false),
            SortItem("Level/Rank(ASC)", query = "level", isAsc = true),
            SortItem("Level/Rank(DESC)", query = "level", isAsc = false),
            SortItem("ATK(ASC)", query = "atk", isAsc = true),
            SortItem("ATK(DESC)", query = "atk", isAsc = false),
            SortItem("DEF(ASC)", query = "def", isAsc = true),
            SortItem("DEF(DESC)", query = "def", isAsc = false)
        )

        fun getItems(): List<SortItem> {
            return list
        }

        val defaultSortType = list[0]
    }
}