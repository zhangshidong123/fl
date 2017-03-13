package com.plmz.fl.http;


public class URLClass {
	//http://42.121.90.220:9101/UrlProxy.ashx?u=http://www.dhfun.cn/list.php*cat=0
	public static final String HOST = "http://42.121.90.220:9101";
	public static final String ASHX = "/UrlProxy.ashx";
	public static final String MAIN_URL = "http://www.dehua.la";//电影推荐http://www.dhfun.cn
	public static final String PLAY_URL = "http://www.dehua.la";
	public static final String TAB_URL = MAIN_URL+"/mlist/%s.html";//电影,动漫，综艺分页_1
	public static final String DIANSHIJU_MAIN_URL = MAIN_URL+"/list.php?cat=10&year=&lang=&area=&actor=&state=&order=0&page=1";//电视剧最新
	public static final String DIANSHIJU_TAB_URL = MAIN_URL+"/list.php?cat=10&year=&lang=&area=%s&actor=&state=&order=0&page=%s";//电视剧分类
	public static final String TVB_URL = MAIN_URL+"/search.php?stype=area&sword=TVB";//TVB分页_2.html&page=2
	//	西瓜
	public static final String XG_URL = "https://www.bu370.com";//https://www.711tu.com/htm/downlist1/   https://www.311hu.com/htm/downlist1/
	public static final String XG_URL_PAGE = "%s.htm";
	//先锋
	public static final String XF_URL = "http://www.luavav.net";
	public static final String XF_URL_GROUP = "%s.html";
	//	public static final String TAB_URL = "http://42.121.90.220:9101/UrlProxy.ashx?u=http://www.dhfun.cn/list.php*cat=%d$year=$lang=$area=$actor=$state=$order=1$page=%d";

}
