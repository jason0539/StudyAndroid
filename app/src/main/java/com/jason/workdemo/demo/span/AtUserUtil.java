package com.jason.workdemo.demo.span;

import android.text.TextUtils;
import android.util.LruCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuzhenhui on 2016/12/8.
 */
public class AtUserUtil {
    public static final String TAG = AtUserUtil.class.getSimpleName();
    static final Pattern atPattern = Pattern.compile("@\\{uid:\\d+,nick:[\\S\\s]+?\\}");

    static final String atFormat = "@{uid:%d,nick:%s}";

    static final String PREFIX = "@{";

    static final String ENDFIX = "}";

    static final String UIDPREFIX = "uid:";

    static final String NICKPREFIX = "nick:";

    static final String SEPARATOR = ",";

    public static LruCache<Long, String> lruMap = new LruCache<Long, String>(50);

    /**
     * 从desc中解析出符合格式的用户列表
     * 格式：@{uid:10000,nick:solo}
     *
     * @param description
     * @return
     */
    public static ArrayList<AtUser> getAtUsers(String description) {
        if (TextUtils.isEmpty(description))
            return null;

        Matcher matcher = atPattern.matcher(description);

        ArrayList<AtUser> atUsers = new ArrayList<>();
        while (matcher.find()) {
            String originString = matcher.group();
            AtUser atUser = getAtUser(originString);
            if (atUser != null) {
                atUsers.add(atUser);
            }
        }
        return atUsers;
    }

    public static AtUser getAtUser(String description) {
        if (TextUtils.isEmpty(description)) {
            return null;
        }

        if (description.startsWith(PREFIX) && description.endsWith(ENDFIX)) {
            String atString = description.substring(PREFIX.length(), description.length() - 1);
            String[] splitResult = atString.split(SEPARATOR, 2);
            List<String> values = new ArrayList<String>(Arrays.asList(splitResult));

            if (values != null && !values.isEmpty() && values.size() == 2) {
                AtUser atUser = new AtUser();
                atUser.userId = Long.valueOf(values.get(0).substring(UIDPREFIX.length()));
                atUser.userNick = values.get(1).substring(NICKPREFIX.length());
                return atUser;
            }
        }
        return null;

    }

    public static String replaceAtUserString(String description, ArrayList<AtUser> atUsers) {
        if (atUsers != null && atUsers.size() > 0) {
            Iterator<AtUser> itera = atUsers.iterator();
            while (itera.hasNext()) {
                AtUser atUser = itera.next();
                String temp = String.format("@{uid:%d,nick:%s}", atUser.userId, atUser.userNick);
                String target = "@".concat(atUser.userNick).concat(" ");
                description = description.replace(temp, target);
            }
        }
        return description;
    }

    public static String replaceLinksStr(String linkStr, ArrayList<AddLinks> linkStrList) {
        if (linkStrList != null && linkStrList.size() > 0) {
            for (int i = 0; i < linkStrList.size(); i++) {
                String temp = String.format("{link:%s}", linkStrList.get(i).getLink());
                linkStr = linkStr.replace(temp, " #网页链接");
            }
        }
        return linkStr;
    }

    public static String replaceAtUserInDesc(String description, ArrayList<AtUser> atUserList) {
        //对于at在后面的,直接找@的起始位置
        StringBuilder desc = new StringBuilder(description.substring(0, description.indexOf("@{uid:")));

        Iterator<AtUser> itera = atUserList.iterator();
        while (itera.hasNext()) {
            AtUser atUser = itera.next();
            desc.append("@");
            desc.append(atUser.userNick);
            desc.append(" ");
        }
        return desc.toString();

    }

    public static String removeAtUserString(long opusId, String description) {

        if (lruMap.get(opusId) != null)
            return lruMap.get(opusId);
        else {
            String result = removeAtUserString(description);
            lruMap.put(opusId, result);
            return result;
        }
    }

    public static String removeAtUserString(String description) {
        if (TextUtils.isEmpty(description))
            return "";
        Matcher matcher = atPattern.matcher(description);

        ArrayList<String> atUsers = new ArrayList<>();
        while (matcher.find()) {
            String originString = matcher.group();
            atUsers.add(originString);
        }
        Iterator<String> itera = atUsers.iterator();
        while (itera.hasNext()) {
            description = description.replace(itera.next(), "");
        }
        return description;
    }

}
