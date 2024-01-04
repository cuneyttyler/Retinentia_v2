//package com.cnytync.retinentia.v2.data
//
//import com.folioreader.data.Book
//
//data class Book (
//    val id: Long,
//    val name: String,
//    val author: String,
//    val fileName: String,
//    var localPath: String,
//    val cover: String,
//    var downloaded: Boolean,
//    var entities: List<Entity> = arrayListOf<Entity>()
//) {
//    fun toFolioBook(): Book {
//        val folioEntities: ArrayList<com.folioreader.data.Entity> = arrayListOf<com.folioreader.data.Entity>()
//
//        if(entities != null) {
//            for(e in entities) {
//                folioEntities.add(e.toFolioEntity())
//            }
//        }
//
//        return com.folioreader.data.Book(
//            id,
//            name,
//            author,
//            fileName,
//            localPath,
//            cover,
//            folioEntities
//        )
//    }
//}