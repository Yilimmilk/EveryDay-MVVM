package cn.mapotofu.everydaymvvm.app

import cn.mapotofu.everydaymvvm.data.model.entity.AboutItem

/**
 * @description
 * @package cn.mapotofu.everyday.base
 * @author milk
 * @date 2021/6/22
 */
object Constants {
    const val URL_REMOTE_API_SERVER = "https://api.mapotofu.cn/v1/"
    const val URL_LOCAL_API_SERVER = "http://192.168.50.173:8000/"
    const val CLIENT_TYPE = 1
    const val MAX_WEEK = 24
    const val MAX_SESSION = 12

    const val APP_WIDGET_ID = "app_widget_id"

    val COLOR_PALETTE = mutableListOf(
        "#CCE57373",
        "#CC7986CB",
        "#CC64B5F6",
        "#CC81C784",
        "#CC4DB6AC",
        "#CC947A6D",
        "#CCFFA726",
        "#CCF19EC2",
        "#CC7A7E23",
        "#CCFBDA41",
        "#CC1B813E",
        "#CCE16B8C",
        "#CCEF3473",
        "#CC005CAF",
        "#CCB28FCE",
        "#CCC1328E",
        "#CC787878",
        "#CC904840"
    )

    //参考项目
    val LIST_OPENSOURCE_REFER = mutableListOf(
        AboutItem("ScheduleX:Surine", "特别感谢此项目，参考了不少代码结构", "https://github.com/Surine/ScheduleX"),
        AboutItem(
            "NWUPL_Pure_EMS:dream2333",
            "也很感谢此项目，让我第一次接触到MVVM设计架构",
            "https://github.com/dream2333/NWUPL_Pure_EMS"
        ),
        AboutItem(
            "zfnew_webApi:jokerwho",
            "也很感谢此项目，初版Python后台由此项目改写而成，由于业务需求有大部分不同，第二版Python后台少量参考此项目完全重写，也是这个项目让我第一次接触到Python，也感谢作者对我的帮助，有一说一，Python真的简单又好用yyds",
            "https://github.com/jokerwho/zfnew_webApi"
        ),
        AboutItem(
            "Crawl-JWGL:always0108",
            "初版Java后台由此项目修改而成，由于后期业务需求比较大，且后台需要UI界面，遂放弃Java转向Python，也很感谢这个项目吧",
            "https://github.com/always0108/Crawl-JWGL"
        ),
        AboutItem(
            "WakeupSchedule_Kotlin:YZune",
            "参考了此项目的设置页面，而且我好喜欢这个项目的理念，由苏州大学WakeUp俱乐部开发，其中'不辜负每一个清晨'的理念也启发了我，使我创立此'朝暮'项目，虽然参考的代码很少，但我依然很感谢，这种理念真的很棒！！！",
            "https://github.com/YZune/WakeupSchedule_Kotlin"
        )
    )

    //引用项目
    val LIST_OPENSOURCE_IMPL = mutableListOf(
        AboutItem(
            "Kotlin",
            "It is an open-source, statically typed programming language supported and developed by JetBrains and open-source contributors",
            "https://github.com/JetBrains/kotlin"
        ),
        AboutItem(
            "Android Jetpack",
            "Jetpack 是一个由多个库组成的套件，可帮助开发者遵循最佳做法、减少样板代码并编写可在各种 Android 版本和设备中一致运行的代码，让开发者可将精力集中于真正重要的编码工作。",
            "https://developer.android.google.cn/jetpack"
        ),
        AboutItem(
            "Material Design",
            "Material is a design system created by Google to help teams build high-quality digital experiences for Android, iOS, Flutter, and the web.",
            "https://material.io/design/introduction"
        ),
        AboutItem(
            "Tencent MMKV",
            "MMKV is an efficient, small, easy-to-use mobile key-value storage framework used in the WeChat application. It's currently available on Android, iOS/macOS, Win32 and POSIX.",
            "https://github.com/Tencent/MMKV"
        ),
        AboutItem(
            "OkHttp",
            "OkHttp is an HTTP client",
            "https://github.com/square/okhttp"
        ),
        AboutItem(
            "Retrofit",
            "A type-safe HTTP client for Android and Java",
            "https://github.com/square/retrofit"
        ),
        AboutItem(
            "Moshi",
            "Moshi is a modern JSON library for Android and Java. It makes it easy to parse JSON into Java objects",
            "https://github.com/square/moshi"
        ),
        AboutItem(
            "JetpackMvvm:hegaojian",
            "一个Jetpack结合MVVM的快速开发框架",
            "https://github.com/hegaojian/JetpackMvvm"
        ),
        AboutItem(
            "BaseRecyclerViewAdapterHelper:CymChad",
            "Powerful and flexible RecyclerView Adapter",
            "https://github.com/CymChad/BaseRecyclerViewAdapterHelper"
        ),
        AboutItem(
            "material-dialogs:afollestad",
            "A beautiful, fluid, and extensible dialogs API for Kotlin & Android.",
            "https://github.com/afollestad/material-dialogs"
        ),
//        AboutItem(
//            "AndroidAutoSize:JessYan",
//            "A low-cost Android screen adaptation solution (今日头条屏幕适配方案终极版，一个极低成本的 Android 屏幕适配方案).",
//            "https://github.com/JessYanCoding/AndroidAutoSize"
//        ),
        AboutItem(
            "CustomActivityOnCrash:Ereza",
            "Android library that allows launching a custom activity when your app crashes, instead of showing the hated \"Unfortunately, X has stopped\" dialog.",
            "https://github.com/Ereza/CustomActivityOnCrash"
        ),
        AboutItem(
            "Splitties:LouisCAD",
            "A collection of hand-crafted extensions for your Kotlin projects.",
            "https://github.com/LouisCAD/Splitties"
        )
    )
}