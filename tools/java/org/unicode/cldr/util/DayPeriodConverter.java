package org.unicode.cldr.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.unicode.cldr.util.DayPeriods.DayPeriod;

import com.ibm.icu.dev.util.CollectionUtilities;
import com.ibm.icu.dev.util.Relation;
import com.ibm.icu.util.ULocale;

public class DayPeriodConverter {
    private static final boolean TO_CODE = true;

    // HACK TO SET UP DATA
    // Will be replaced by real data table in the future

    static final String[][] RAW_DATA = {
        {"en", "0", "NIGHT1", "night"},
        {"en", "6", "MORNING1", "morning"},
        {"en", "12", "AFTERNOON1", "afternoon"},
        {"en", "18", "EVENING1", "evening"},
        {"en", "21", "NIGHT1", "night"},
        {"af", "0", "NIGHT1", "nag"},
        {"af", "5", "MORNING1", "oggend"},
        {"af", "12", "AFTERNOON1", "middag"},
        {"af", "18", "EVENING1", "aand"},
        {"nl", "0", "NIGHT1", "nacht"},
        {"nl", "6", "MORNING1", "ochtend"},
        {"nl", "12", "AFTERNOON1", "middag"},
        {"nl", "18", "EVENING1", "avond"},
        {"de", "0", "NIGHT1", "Nacht"},
        {"de", "5", "MORNING1", "Morgen"},
        {"de", "10", "MORNING2", "Vormittag"},
        {"de", "12", "AFTERNOON1", "Mittag"},
        {"de", "13", "AFTERNOON2", "Nachmittag"},
        {"de", "18", "EVENING1", "Abend"},
        {"da", "0", "NIGHT1", "nat"},
        {"da", "5", "MORNING1", "morgen"},
        {"da", "10", "MORNING2", "formiddag"},
        {"da", "12", "AFTERNOON1", "eftermiddag"},
        {"da", "18", "EVENING1", "aften"},
        {"nb", "0", "NIGHT1", "natt"},
        {"nb", "6", "MORNING1", "morgen"},
        {"nb", "10", "MORNING2", "formiddag"},
        {"nb", "12", "AFTERNOON1", "ettermiddag"},
        {"nb", "18", "EVENING1", "kveld"},
        {"sv", "0", "NIGHT1", "natt"},
        {"sv", "5", "MORNING1", "morgon"},
        {"sv", "10", "MORNING2", "förmiddag"},
        {"sv", "12", "AFTERNOON1", "eftermiddag"},
        {"sv", "18", "EVENING1", "kväll"},
        {"is", "0", "NIGHT1", "nótt"},
        {"is", "6", "MORNING1", "morgunn"},
        {"is", "12", "AFTERNOON1", "eftir hádegi"},
        {"is", "18", "EVENING1", "kvöld"},
        {"pt", "0", "NIGHT1", "madrugada"},
        {"pt", "6", "MORNING1", "manhã"},
        {"pt", "12", "AFTERNOON1", "tarde"},
        {"pt", "19", "EVENING1", "noite"},
        {"gl", "0", "MORNING1", "madrugada"},
        {"gl", "6", "MORNING2", "mañá"},
        {"gl", "12", "AFTERNOON1", "mediodía"},
        {"gl", "13", "EVENING1", "tarde"},
        {"gl", "21", "NIGHT1", "noite"},
        {"es", "0", "MORNING1", "madrugada"},
        {"es", "6", "MORNING2", "mañana"},
        {"es", "12", "EVENING1", "tarde"},
        {"es", "20", "NIGHT1", "noche"},
        {"ca", "0", "MORNING1", "matinada"},
        {"ca", "6", "MORNING2", "matí"},
        {"ca", "12", "AFTERNOON1", "migdia"},
        {"ca", "13", "AFTERNOON2", "tarda"},
        {"ca", "19", "EVENING1", "vespre"},
        {"ca", "21", "NIGHT1", "nit"},
        {"it", "0", "NIGHT1", "notte"},
        {"it", "6", "MORNING1", "mattina"},
        {"it", "12", "AFTERNOON1", "pomeriggio"},
        {"it", "18", "EVENING1", "sera"},
        {"ro", "0", "NIGHT1", "noapte"},
        {"ro", "5", "MORNING1", "dimineață"},
        {"ro", "12", "AFTERNOON1", "după-amiază"},
        {"ro", "18", "EVENING1", "seară"},
        {"ro", "22", "NIGHT1", "noapte"},
        {"fr", "0", "NIGHT1", "nuit"},
        {"fr", "4", "MORNING1", "matin"},
        {"fr", "12", "AFTERNOON1", "après-midi"},
        {"fr", "18", "EVENING1", "soir"},
        {"hr", "0", "NIGHT1", "noć"},
        {"hr", "4", "MORNING1", "jutro"},
        {"hr", "12", "AFTERNOON1", "popodne"},
        {"hr", "18", "EVENING1", "večer"},
        {"hr", "21", "NIGHT1", "noć"},
        {"bs", "0", "NIGHT1", "noć"},
        {"bs", "4", "MORNING1", "jutro"},
        {"bs", "12", "AFTERNOON1", "popodne"},
        {"bs", "18", "EVENING1", "veče"},
        {"bs", "21", "NIGHT1", "noć"},
        {"sr", "0", "NIGHT1", "ноћ"},
        {"sr", "6", "MORNING1", "јутро"},
        {"sr", "12", "AFTERNOON1", "поподне"},
        {"sr", "18", "EVENING1", "вече"},
        {"sr", "21", "NIGHT1", "ноћ"},
        {"sl", "0", "NIGHT1", "noč"},
        {"sl", "6", "MORNING1", "jutro"},
        {"sl", "10", "MORNING2", "dopoldne"},
        {"sl", "12", "AFTERNOON1", "popoldne"},
        {"sl", "18", "EVENING1", "večer"},
        {"sl", "22", "NIGHT1", "noč"},
        {"cs", "0", "NIGHT1", "noc"},
        {"cs", "4", "MORNING1", "ráno"},
        {"cs", "9", "MORNING2", "dopoledne"},
        {"cs", "12", "AFTERNOON1", "odpoledne"},
        {"cs", "18", "EVENING1", "večer"},
        {"cs", "22", "NIGHT1", "noc"},
        {"sk", "0", "NIGHT1", "noc"},
        {"sk", "4", "MORNING1", "ráno"},
        {"sk", "9", "MORNING2", "dopoludnie"},
        {"sk", "12", "AFTERNOON1", "popoludnie"},
        {"sk", "18", "EVENING1", "večer"},
        {"sk", "22", "NIGHT1", "noc"},
        {"pl", "0", "NIGHT1", "noc"},
        {"pl", "6", "MORNING1", "rano"},
        {"pl", "10", "MORNING2", "przedpołudnie"},
        {"pl", "12", "AFTERNOON1", "popołudnie"},
        {"pl", "18", "EVENING1", "wieczór"},
        {"pl", "21", "NIGHT1", "noc"},
        {"bg", "0", "NIGHT1", "нощ"},
        {"bg", "4", "MORNING1", "сутринта"},
        {"bg", "11", "MORNING2", "на обяд"},
        {"bg", "14", "AFTERNOON1", "следобяд"},
        {"bg", "18", "EVENING1", "вечерта"},
        {"bg", "22", "NIGHT1", "нощ"},
        {"mk", "0", "NIGHT1", "по полноќ"},
        {"mk", "4", "MORNING1", "наутро"},
        {"mk", "10", "MORNING2", "претпладне"},
        {"mk", "12", "AFTERNOON1", "попладне"},
        {"mk", "18", "EVENING1", "навечер"},
        {"ru", "0", "NIGHT1", "ночь"},
        {"ru", "4", "MORNING1", "утро"},
        {"ru", "12", "AFTERNOON1", "день"},
        {"ru", "18", "EVENING1", "вечер"},
        {"uk", "0", "NIGHT1", "ніч"},
        {"uk", "4", "MORNING1", "ранок"},
        {"uk", "12", "AFTERNOON1", "день"},
        {"uk", "18", "EVENING1", "вечір"},
        {"lt", "0", "NIGHT1", "naktis"},
        {"lt", "6", "MORNING1", "rytas"},
        {"lt", "12", "AFTERNOON1", "diena"},
        {"lt", "18", "EVENING1", "vakaras"},
        {"lv", "0", "NIGHT1", "nakts"},
        {"lv", "6", "MORNING1", "rīts"},
        {"lv", "12", "AFTERNOON1", "pēcpusdiena"},
        {"lv", "18", "EVENING1", "vakars"},
        {"lv", "23", "NIGHT1", "nakts"},
        {"el", "0", "NIGHT1", "βράδυ"},
        {"el", "4", "MORNING1", "πρωί"},
        {"el", "12", "AFTERNOON1", "μεσημέρι"},
        {"el", "17", "EVENING1", "απόγευμα"},
        {"el", "20", "NIGHT1", "βράδυ"},
        {"fa", "0", "NIGHT1", "شب"},
        {"fa", "4", "MORNING1", "صبح"},
        {"fa", "12", "AFTERNOON1", "بعد از ظهر"},
        {"fa", "17", "EVENING1", "عصر"},
        {"fa", "19", "NIGHT1", "شب"},
        {"hy", "0", "NIGHT1", "գիշեր"},
        {"hy", "6", "MORNING1", "առավոտ"},
        {"hy", "12", "AFTERNOON1", "ցերեկ"},
        {"hy", "18", "EVENING1", "երեկո"},
        {"ka", "0", "NIGHT1", "ღამე"},
        {"ka", "5", "MORNING1", "დილა"},
        {"ka", "12", "AFTERNOON1", "ნაშუადღევი"},
        {"ka", "18", "EVENING1", "საღამო"},
        {"ka", "21", "NIGHT1", "ღამე"},
        {"sq", "0", "NIGHT1", "natë"},
        {"sq", "4", "MORNING1", "mëngjes"},
        {"sq", "9", "MORNING2", "paradite"},
        {"sq", "12", "AFTERNOON1", "pasdite"},
        {"sq", "18", "EVENING1", "mbrëmje"},
        {"ur", "0", "NIGHT1", "رات"},
        {"ur", "4", "MORNING1", "صبح"},
        {"ur", "12", "AFTERNOON1", "دوپہر"},
        {"ur", "16", "AFTERNOON2", "سہ پہر"},
        {"ur", "18", "EVENING1", "شام"},
        {"ur", "20", "NIGHT1", "رات"},
        {"hi", "0", "NIGHT1", "रात"},
        {"hi", "4", "MORNING1", "सुबह"},
        {"hi", "12", "AFTERNOON1", "दोपहर"},
        {"hi", "16", "EVENING1", "शाम"},
        {"hi", "20", "NIGHT1", "रात"},
        {"bn", "0", "NIGHT1", "রাত্রি"},
        {"bn", "4", "MORNING1", "ভোর"},
        {"bn", "6", "MORNING2", "সকাল"},
        {"bn", "12", "AFTERNOON1", "দুপুর"},
        {"bn", "16", "AFTERNOON2", "বিকাল"},
        {"bn", "18", "EVENING1", "সন্ধ্যা"},
        {"bn", "20", "NIGHT1", "রাত্রি"},
        {"gu", "0", "NIGHT1", "રાત"},
        {"gu", "4", "MORNING1", "સવાર"},
        {"gu", "12", "AFTERNOON1", "બપોર"},
        {"gu", "16", "EVENING1", "સાંજ"},
        {"gu", "20", "NIGHT1", "રાત"},
        {"mr", "0", "NIGHT1", "रात्री"},
        {"mr", "3", "NIGHT2", "रात्र"},
        {"mr", "4", "MORNING1", "पहाटे"},
        {"mr", "6", "MORNING2", "सकाळी"},
        {"mr", "12", "AFTERNOON1", "दुपारी"},
        {"mr", "16", "EVENING1", "संध्याकाळी"},
        {"mr", "20", "NIGHT1", "रात्री"},
        {"ne", "0", "NIGHT1", "रात"},
        {"ne", "4", "MORNING1", "विहान"},
        {"ne", "12", "AFTERNOON1", "अपरान्ह"},
        {"ne", "16", "AFTERNOON2", "साँझ"},
        {"ne", "19", "EVENING1", "बेलुका"},
        {"ne", "22", "NIGHT1", "रात"},
        {"pa", "0", "NIGHT1", "ਰਾਤ"},
        {"pa", "4", "MORNING1", "ਸਵੇਰ"},
        {"pa", "12", "AFTERNOON1", "ਦੁਪਹਿਰ"},
        {"pa", "16", "EVENING1", "ਸ਼ਾਮ"},
        {"pa", "21", "NIGHT1", "ਰਾਤ"},
        {"si", "0", "NIGHT2", "මැදියමට පසු"},
        {"si", "1", "MORNING1", "පාන්දර"},
        {"si", "6", "MORNING2", "උදේ"},
        {"si", "12", "AFTERNOON1", "දවල්"},
        {"si", "14", "EVENING1", "හවස"},
        {"si", "18", "NIGHT1", "රෑ"},
        {"ta", "0", "NIGHT1", "இரவு"},
        {"ta", "3", "MORNING1", "அதிகாலை"},
        {"ta", "5", "MORNING2", "காலை"},
        {"ta", "12", "AFTERNOON1", "மதியம்"},
        {"ta", "14", "AFTERNOON2", "பிற்பகல்"},
        {"ta", "16", "EVENING1", "மாலை"},
        {"ta", "18", "EVENING2", "அந்தி மாலை"},
        {"ta", "21", "NIGHT1", "இரவு"},
        {"te", "0", "NIGHT1", "రాత్రి"},
        {"te", "6", "MORNING1", "ఉదయం"},
        {"te", "12", "AFTERNOON1", "మధ్యాహ్నం"},
        {"te", "18", "EVENING1", "సాయంత్రం"},
        {"te", "21", "NIGHT1", "రాత్రి"},
        {"ml", "0", "NIGHT1", "രാത്രി"},
        {"ml", "3", "MORNING1", "പുലർച്ചെ"},
        {"ml", "6", "MORNING2", "രാവിലെ"},
        {"ml", "12", "AFTERNOON1", "ഉച്ചയ്ക്ക്"},
        {"ml", "14", "AFTERNOON2", "ഉച്ചതിരിഞ്ഞ്"},
        {"ml", "15", "EVENING1", "വൈകുന്നേരം"},
        {"ml", "18", "EVENING2", "സന്ധ്യയ്ക്ക്"},
        {"ml", "19", "NIGHT1", "രാത്രി"},
        {"kn", "0", "NIGHT1", "ರಾತ್ರಿ"},
        {"kn", "6", "MORNING1", "ಬೆಳಗ್ಗೆ"},
        {"kn", "12", "AFTERNOON1", "ಮಧ್ಯಾಹ್ನ"},
        {"kn", "18", "EVENING1", "ಸಂಜೆ"},
        {"kn", "21", "NIGHT1", "ರಾತ್ರಿ"},
        {"zh", "0", "NIGHT1", "凌晨"},
        {"zh", "5", "MORNING1", "早上"},
        {"zh", "8", "MORNING2", "上午"},
        {"zh", "12", "AFTERNOON1", "中午"},
        {"zh", "13", "AFTERNOON2", "下午"},
        {"zh", "19", "EVENING1", "晚上"},
        {"ja", "0", "NIGHT2", "夜中"},
        {"ja", "4", "MORNING1", "朝"},
        {"ja", "12", "AFTERNOON1", "昼"},
        {"ja", "16", "EVENING1", "夕方"},
        {"ja", "19", "NIGHT1", "夜"},
        {"ja", "23", "NIGHT2", "夜中"},
        {"ko", "0", "NIGHT1", "밤"},
        {"ko", "3", "MORNING1", "새벽"},
        {"ko", "6", "MORNING2", "오전"},
        {"ko", "12", "AFTERNOON1", "오후"},
        {"ko", "18", "EVENING1", "저녁"},
        {"ko", "21", "NIGHT1", "밤"},
        {"tr", "0", "NIGHT1", "gece"},
        {"tr", "6", "MORNING1", "sabah"},
        {"tr", "11", "MORNING2", "öğleden önce"},
        {"tr", "12", "AFTERNOON1", "öğleden sonra"},
        {"tr", "18", "AFTERNOON2", "akşamüstü"},
        {"tr", "19", "EVENING1", "akşam"},
        {"tr", "21", "NIGHT1", "gece"},
        {"az", "0", "NIGHT2", "gecə"},
        {"az", "4", "MORNING1", "sübh"},
        {"az", "6", "MORNING2", "səhər"},
        {"az", "12", "AFTERNOON1", "gündüz"},
        {"az", "17", "EVENING1", "axşamüstü"},
        {"az", "19", "NIGHT1", "axşam"},
        {"kk", "0", "NIGHT1", "түн"},
        {"kk", "6", "MORNING1", "таң"},
        {"kk", "12", "AFTERNOON1", "түс"},
        {"kk", "18", "EVENING1", "кеш"},
        {"kk", "21", "NIGHT1", "түн"},
        {"ky", "0", "NIGHT1", "түн"},
        {"ky", "6", "MORNING1", "эртең менен"},
        {"ky", "12", "AFTERNOON1", "түштөн кийин"},
        {"ky", "18", "EVENING1", "кечкурун"},
        {"ky", "21", "NIGHT1", "түн"},
        {"uz", "0", "NIGHT1", "tun"},
        {"uz", "6", "MORNING1", "ertalab"},
        {"uz", "11", "AFTERNOON1", "kunduzi"},
        {"uz", "18", "EVENING1", "kechqurun"},
        {"uz", "22", "NIGHT1", "tun"},
        {"et", "0", "NIGHT1", "öö"},
        {"et", "5", "MORNING1", "hommik"},
        {"et", "12", "AFTERNOON1", "pärastlõuna"},
        {"et", "18", "EVENING1", "õhtu"},
        {"et", "23", "NIGHT1", "öö"},
        {"fi", "0", "NIGHT1", "yö"},
        {"fi", "5", "MORNING1", "aamu"},
        {"fi", "10", "MORNING2", "aamupäivä"},
        {"fi", "12", "AFTERNOON1", "iltapäivä"},
        {"fi", "18", "EVENING1", "ilta"},
        {"fi", "23", "NIGHT1", "yö"},
        {"hu", "0", "NIGHT1", "éjjel"},
        {"hu", "4", "NIGHT2", "hajnal"},
        {"hu", "6", "MORNING1", "reggel"},
        {"hu", "9", "MORNING2", "délelőtt"},
        {"hu", "12", "AFTERNOON1", "délután"},
        {"hu", "18", "EVENING1", "este"},
        {"hu", "21", "NIGHT1", "éjjel"},
        {"th", "0", "NIGHT1", "กลางคืน"},
        {"th", "6", "MORNING1", "เช้า"},
        {"th", "12", "AFTERNOON1", "เที่ยง"},
        {"th", "13", "AFTERNOON2", "บ่าย"},
        {"th", "16", "EVENING1", "เย็น"},
        {"th", "18", "EVENING2", "ค่ำ"},
        {"th", "21", "NIGHT1", "กลางคืน"},
        {"lo", "0", "NIGHT1", "​ກາງ​ຄືນ"},
        {"lo", "5", "MORNING1", "​ເຊົ້າ"},
        {"lo", "12", "AFTERNOON1", "ສວຍ"},
        {"lo", "16", "EVENING1", "ແລງ"},
        {"lo", "17", "EVENING2", "​ແລງ"},
        {"lo", "20", "NIGHT1", "​ກາງ​ຄືນ"}, // "ກາງ​ຄືນ"},
        {"ar", "0", "NIGHT1", "منتصف الليل"},
        {"ar", "1", "NIGHT2", "ليلا"},
        {"ar", "3", "MORNING1", "فجرا"},
        {"ar", "6", "MORNING2", "صباحا"},
        {"ar", "12", "AFTERNOON1", "ظهرا"},
        {"ar", "13", "AFTERNOON2", "بعد الظهر"},
        {"ar", "18", "EVENING1", "مساء"},
        {"he", "0", "NIGHT1", "לילה"},
        {"he", "5", "MORNING1", "בוקר"},
        {"he", "11", "AFTERNOON1", "צהריים"},
        {"he", "15", "AFTERNOON2", "אחר הצהריים"},
        {"he", "18", "EVENING1", "ערב"},
        {"he", "22", "NIGHT1", "לילה"},
        {"id", "0", "MORNING1", "pagi"},
        {"id", "10", "AFTERNOON1", "siang"},
        {"id", "15", "EVENING1", "sore"},
        {"id", "18", "NIGHT1", "malam"},
        {"ms", "0", "MORNING1", "tengah malam"},
        {"ms", "1", "MORNING2", "pagi"},
        {"ms", "12", "AFTERNOON1", "tengah hari"},
        {"ms", "14", "EVENING1", "petang"},
        {"ms", "19", "NIGHT1", "malam"},
        {"fil", "0", "MORNING1", "madaling-araw"},
        {"fil", "6", "MORNING2", "umaga"},
        {"fil", "12", "AFTERNOON1", "tanghali"},
        {"fil", "16", "EVENING1", "hapon"},
        {"fil", "18", "NIGHT1", "gabi"},
        {"vi", "0", "NIGHT1", "đêm"},
        {"vi", "4", "MORNING1", "sáng"},
        {"vi", "12", "AFTERNOON1", "chiều"},
        {"vi", "18", "EVENING1", "tối"},
        {"vi", "21", "NIGHT1", "đêm"},
        {"km", "0", "MORNING1", "ព្រឹក"},
        {"km", "12", "AFTERNOON1", "រសៀល"},
        {"km", "18", "EVENING1", "ល្ងាច"},
        {"km", "21", "NIGHT1", "យប់"},
        {"sw", "0", "NIGHT1", "usiku"},
        {"sw", "4", "MORNING1", "alfajiri"},
        {"sw", "7", "MORNING2", "asubuhi"},
        {"sw", "12", "AFTERNOON1", "mchana"},
        {"sw", "16", "EVENING1", "jioni"},
        {"sw", "19", "NIGHT1", "usiku"},
        {"zu", "0", "MORNING1", "ntathakusa"},
        {"zu", "6", "MORNING2", "ekuseni"},
        {"zu", "10", "AFTERNOON1", "emini"},
        {"zu", "13", "EVENING1", "ntambama"},
        {"zu", "19", "NIGHT1", "ebusuku"},
        {"am", "0", "NIGHT1", "ሌሊት"},
        {"am", "6", "MORNING1", "ጥዋት"},
        {"am", "12", "AFTERNOON1", "ከሰዓት በኋላ"},
        {"am", "18", "EVENING1", "ማታ"},
        {"eu", "0", "MORNING1", "goizaldea"},
        {"eu", "6", "MORNING2", "goiza"},
        {"eu", "12", "AFTERNOON1", "eguerdia"},
        {"eu", "14", "AFTERNOON2", "arratsaldea"},
        {"eu", "19", "EVENING1", "iluntzea"},
        {"eu", "21", "NIGHT1", "gaua"},
        {"mn", "0", "NIGHT1", "шөнө"},
        {"mn", "6", "MORNING1", "өглөө"},
        {"mn", "12", "AFTERNOON1", "өдөр"},
        {"mn", "18", "EVENING1", "орой"},
        {"mn", "21", "NIGHT1", "шөнө"},
        {"my", "0", "MORNING1", "နံနက်"},
        {"my", "12", "AFTERNOON1", "နေ့လည်"},
        {"my", "16", "EVENING1", "ညနေ"},
        {"my", "19", "NIGHT1", "ည"},
    };

    static class DayInfo {
        ULocale locale;
        DayPeriods.DayPeriod[] data = new DayPeriod[24];
        Map<DayPeriod,String> toNativeName = new EnumMap<DayPeriod,String>(DayPeriod.class);
        Map<String, DayPeriod> toDayPeriod = new HashMap<String,DayPeriod>();
        @Override
        public String toString() {
            String result = "make(\"" + locale + "\"";
            DayPeriod lastDayPeriod = null;
            for (int i = 0; i < 24; ++i) {
                DayPeriod dayPeriod = data[i];
                if (dayPeriod != lastDayPeriod) {
                    result += ")\n.add(\""
                        + dayPeriod
                        + "\", \""
                        + toNativeName.get(dayPeriod)
                        + "\"";
                    lastDayPeriod = dayPeriod;
                }
                result += ", " + i;
            }
            result += ")\n.build();\n";
            /*
            make("en")
           .add("MORNING", "morning", 6, 7, 8, 9, 10, 11)
           .add("AFTERNOON", "afternoon", 12, 13, 14, 15, 16, 17)
           .add("EVENING", "evening", 18, 19, 20)
           .add("NIGHT", "night", 0, 1, 2, 3, 4, 5, 21, 22, 23)
           .build();
             */
            return result;
        }
    }

    static final Map<ULocale,DayInfo> DATA = new LinkedHashMap<>();
    static {
        for (String[] x : RAW_DATA) {
            ULocale locale = new ULocale(x[0]);
            int start = Integer.parseInt(x[1]);
            DayPeriod dayPeriod = DayPeriod.valueOf(x[2]);
            String nativeName = x[3].trim();
            DayInfo data = DATA.get(locale);
            if (data == null) {
                DATA.put(locale, data = new DayInfo());
            }
            data.locale = locale;
            for (int i = start; i < 24; ++i) {
                data.data[i] = dayPeriod;
            }
            String old = data.toNativeName.get(dayPeriod);
            if (old != null && !old.equals(nativeName)) {
                throw new IllegalArgumentException(locale + " inconsistent native name for "
                    + dayPeriod + ", old: «" + old + "», new: «" + nativeName + "»");
            }
            DayPeriod oldDp = data.toDayPeriod.get(nativeName);
            if (oldDp != null && oldDp != dayPeriod) {
                throw new IllegalArgumentException(locale + " inconsistent day periods for name «"
                    + nativeName + "», old: " + oldDp + ", new: " + dayPeriod);
            }
            data.toDayPeriod.put(nativeName, dayPeriod);
            data.toNativeName.put(dayPeriod, nativeName);
        }
    }
    public static void main(String[] args) {
        for ( Entry<ULocale, DayInfo> foo : DATA.entrySet()) {
            check(foo.getKey(), foo.getValue());
            System.out.println(foo.getValue());
        }
    }
    private static void check(ULocale locale, DayInfo value) {
        check(locale, DayPeriod.MORNING1, DayPeriod.MORNING2, value);
        check(locale, DayPeriod.AFTERNOON1, DayPeriod.AFTERNOON2, value);
        check(locale, DayPeriod.EVENING1, DayPeriod.EVENING2, value);
        check(locale, DayPeriod.NIGHT1, DayPeriod.NIGHT2, value);
        DayPeriod lastDp = value.data[23];
        for (DayPeriod dp : value.data) {
            if (lastDp.compareTo(dp) > 0) {
                if ((lastDp == DayPeriod.NIGHT1 || lastDp == DayPeriod.NIGHT2) && dp == DayPeriod.MORNING1) {
                } else {
                    throw new IllegalArgumentException(locale + " " + lastDp + " > " + dp);  
                }
            }
            lastDp = dp;
        }
    }
    private static void check(ULocale locale, DayPeriod morning1, DayPeriod morning2, DayInfo value) {
        if (value.toNativeName.containsKey(morning2) && !value.toNativeName.containsKey(morning1)) {
            throw new IllegalArgumentException(locale + " Contains " + morning2 + ", but not " + morning1);            
        }
    }
}
