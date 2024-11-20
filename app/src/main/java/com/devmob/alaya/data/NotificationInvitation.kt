package com.devmob.alaya.data

data class NotificationInvitation(
    val app_id:String,
    val target_channel:String,
    val contents: Map<String,String>,
    val include_aliases: IncludeAliases,
    val android_priority: Int = 10,
    val android_channel_id: String = "d2504827-ed1b-4728-aa57-911581ad18b6"
)