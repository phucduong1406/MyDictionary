package it15110278.vn.edu.hcmute.vn.mydictionary;

/**
 * Created by ASUS on 6/7/2018.
 */

public class DB {
    public static String[] getData(int id) {
        if (id == R.id.action_ev) {
            return getEngVie();
        } else if (id == R.id.action_ve) {
            return getVieEng();
        }
        return new String[0];
    }

    public static String[] getEngVie() {
        String[] source = new String[]{
                "abandon",
                "abandonment",
                "abeyance",
                "abide",
                "ability",
                "able",
                "about",
                "above",
                "abroad",
                "absence",
                "absent",
                "absolute",
                "absolutely",
                "absorb",
                "abuse",
                "academic",
                "accent",
                "acceptable",
                "access",
                "accident",
                "accidental",
                "accidentally",
                "bid",
                "bill",
                "bind"
        };
        return source;
    }

    public static String[] getVieEng() {
        String[] source = new String[]{
                "a dua",
                "ác",
                "ác độc",
                "ác liệt",
                "am",
                "ám",
                "ám ảnh",
                "ảm chỉ",
                "ảm đảm",
                "án",
                "an khang",
                "ao",
                "áo",
                "ảo ảnh",
                "ào ạt",
                "ba",
                "bà",
                "bạc",
                "bàn",
                "bán",
                "bi",
                "bí",
                "bò",
                "bỏ",
        };
        return source;
    }
}
