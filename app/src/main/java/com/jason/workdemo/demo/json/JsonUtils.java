package com.jason.workdemo.demo.json;

import com.alibaba.fastjson.JSON;
import com.jason.common.utils.MLog;
import com.jason.workdemo.demo.json.bean.RecommendUser;

import java.util.List;

/**
 * Created by liuzhenhui on 2016/12/8.
 */
public class JsonUtils {
    public static final String TAG = JsonUtils.class.getSimpleName();

    public static void parseJson() {
        String content = "{\"Recommend_User\":[{\"From_Account\":\"10000\",\"ProfileItem\":[{\"Tag\":\"Tag_Profile_IM_Nick\",\"Value\":\"拇指君\"},{\"Tag\":\"Tag_Profile_IM_Image\",\"Value\":\"http://7xj84g.com1.z0.glb.clouddn.com/%40%2Fuser%2Favatar%2F0003.jpg\"},{\"Tag\":\"Tag_Profile_Custom_Vtype\",\"Value\":\"https://devmedia.stable.fingerapp.cn/authentication_offical.png\"}],\"Recommend_Reason\":\"你关注的人也关注\"},{\"From_Account\":\"20000\",\"ProfileItem\":[{\"Tag\":\"Tag_Profile_IM_Nick\",\"Value\":\"jason\"},{\"Tag\":\"Tag_Profile_IM_Image\",\"Value\":\"http://7xj84g.com1.z0.glb.clouddn.com/%40%2Fuser%2Favatar%2F0003.jpg\"},{\"Tag\":\"Tag_Profile_Custom_Vtype\",\"Value\":\"https://devmedia.stable.fingerapp.cn/authentication_training.png\"}],\"Recommend_Reason\":\"你的手机好友\"},{\"From_Account\":\"10000440000\",\"ProfileItem\":[{\"Tag\":\"Tag_Profile_IM_Nick\",\"Value\":\"夏飞\"},{\"Tag\":\"Tag_Profile_IM_Image\",\"Value\":\"http://7xj84g.com1.z0.glb.clouddn.com/%40%2Fuser%2Favatar%2F0003.jpg\"},{\"Tag\":\"Tag_Profile_Custom_Vtype\",\"Value\":\"https://devmedia.stable.fingerapp.cn/authentication_active.png\"}],\"Recommend_Reason\":\"你关注的人也关注\"}]}";
        RecommendUser recommendUsers = JSON.parseObject(content, RecommendUser.class);
        List<RecommendUser.UserProfile> recommendUser = recommendUsers.getRecommend_User();
        if (recommendUser != null) {
            MLog.d(MLog.TAG_JSON, TAG + "->" + "parseJson " + recommendUser.size());
        } else {
            MLog.d(MLog.TAG_JSON, TAG + "->" + "parseJson 空");
        }
        for (RecommendUser.UserProfile user : recommendUser) {
            MLog.d(MLog.TAG_JSON, TAG + "->" + "parseJson " + user.getFrom_Account() + ",reason = " + user.getRecommend_Reason());
            List<RecommendUser.UserProfile.ProfileItem> profileItem = user.getProfileItem();
            for (RecommendUser.UserProfile.ProfileItem item : profileItem) {
                MLog.d(MLog.TAG_JSON, TAG + "->" + "parseJson " + item.getTag() + " = " + item.getValue());
            }
        }
    }
}
