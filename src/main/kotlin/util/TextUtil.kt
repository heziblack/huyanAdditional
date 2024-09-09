package icu.heziblack.miraiplugin.chahuyunAdditionalItem.util

object TextUtil {
    /**
     * 开启权限组成功文本 <p/>
     * @param permGroupName 权限组名
     * @return 文本“本群的xx开启成功！”
     * */
    fun addPermSucceed(permGroupName:String):String{
        return "本群的${permGroupName}开启成功!"
    }
    /**
     * 开启权限组失败文本 <p/>
     * @param permGroupName 权限组名
     * @return 文本“本群的xx开启失败！”
     * */
    fun addPermFail(permGroupName:String):String{
        return "本群的${permGroupName}开启失败!"
    }
    /**
     * 重复添加权限组文本 <p/>
     * @param permGroupName 权限组名
     * @return 文本“本群的xx已经开启了！”
     * */
    fun addPermAlreadyHas(permGroupName:String):String{
        return "本群的${permGroupName}已经开启了!"
    }
    /**
     * 除移权限组成功 <p/>
     * @param permGroupName 权限组名
     * @return 文本“本群的xx关闭成功！”
     * */
    fun removePermSucceed(permGroupName:String):String{
        return "本群的${permGroupName}关闭成功!"
    }
    /**
     * 重复移除权限组文本 <p/>
     * @param permGroupName 权限组名
     * @return 文本“本群的xx关闭！”
     * */
    fun removePermAlreadyLost(permGroupName:String):String{
        return "本群的${permGroupName}已经关闭!"
    }
}