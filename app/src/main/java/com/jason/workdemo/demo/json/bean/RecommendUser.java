package com.jason.workdemo.demo.json.bean;


import java.util.List;

/**
 * Created by liuzhenhui on 2016/12/8.
 */
public class RecommendUser {
    public static final String Tag_Profile_IM_Nick = "Tag_Profile_IM_Nick";
    public static final String Tag_Profile_IM_Image = "Tag_Profile_IM_Image";
    public static final String Tag_Profile_Custom_Vtype = "Tag_Profile_Custom_Vtype";

    private List<UserProfile> Recommend_User = null;

    public List<UserProfile> getRecommend_User() {
        return Recommend_User;
    }

    public void setRecommend_User(List<UserProfile> recommendUser) {
        this.Recommend_User = recommendUser;
    }

    public class UserProfile {
        private String From_Account;
        private List<ProfileItem> ProfileItem = null;
        private String Recommend_Reason;

        public String getFrom_Account() {
            return From_Account;
        }

        public void setFrom_Account(String fromAccount) {
            this.From_Account = fromAccount;
        }

        public List<ProfileItem> getProfileItem() {
            return ProfileItem;
        }

        public void setProfileItem(List<ProfileItem> profileItem) {
            this.ProfileItem = profileItem;
        }

        public String getRecommend_Reason() {
            return Recommend_Reason;
        }

        public void setRecommend_Reason(String recommendReason) {
            this.Recommend_Reason = recommendReason;
        }

        public class ProfileItem {
            private String Tag;
            private String Value;

            public String getTag() {
                return Tag;
            }

            public void setTag(String tag) {
                this.Tag = tag;
            }

            public String getValue() {
                return Value;
            }

            public void setValue(String value) {
                this.Value = value;
            }
        }
    }
}
