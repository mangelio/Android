package io.mangel.issuemanager.data.models

class ConstructionSite(val name: String, val address: Address, val imagePath: String) {
    private val _maps = ArrayList<Map>()
    val maps: List<Map> = _maps
}