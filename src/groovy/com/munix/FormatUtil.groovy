package com.munix

class FormatUtil{
    static String createTimeStamp(user){
        return "${user?.userRealName}, " +new Date().format("MMM. dd, yyyy - hh:mm a")
    }

}
