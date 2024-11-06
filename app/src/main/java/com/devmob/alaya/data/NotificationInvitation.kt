package com.devmob.alaya.data

data class NotificationInvitation(
    val app_id:String,
    val target_channel:String,
    val contents: Map<String,String>,
    val include_aliases: IncludeAliases
)