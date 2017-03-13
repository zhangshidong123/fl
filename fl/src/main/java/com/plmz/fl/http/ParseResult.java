package com.plmz.fl.http;

import android.text.TextUtils;

import com.plmz.fl.entity.Movie;
import com.plmz.fl.entity.PlayList;

import java.util.ArrayList;

public class ParseResult {
	//首页推荐
	private static final String TAG1 = "<div class=\"tit\">";
	private static final String TAG2 = "<ul>";
	private static final String TAG3 = "</ul>";
	private static final String TAG4 = "<li>";
	private static final String TAG5 = "</li>";
	//电影详情
	private static final String TAG6 = "ul_url";
	//playUrl
	private static final String TAG7 = "player_main";
	private static final String TAG8 = "url";
	private static final String TAG9 = "</SCRIPT>";

	public static String parseHttpResultTJ(String result) {
		String subStrUl = "";
		if (!TextUtils.isEmpty(result)) {
			int divIndex = result.indexOf(TAG1);
			String subStrDiv = result.substring(divIndex);
			int ulStartIndex = subStrDiv.indexOf(TAG2);
			int ulEndIndex = subStrDiv.indexOf(TAG3);
			subStrUl = subStrDiv.substring(ulStartIndex, ulEndIndex);
		}
		return subStrUl;
	}

	//解析首页推荐电影列表
	public static ArrayList<Movie> parseLi(String tarStr) {
		tarStr = parseHttpResultTJ(tarStr);
		ArrayList<Movie> list = new ArrayList<Movie>();
		boolean ret = true;
		while (ret) {
			try {
				if (tarStr.indexOf(TAG4) != -1) {
					int start = tarStr.indexOf("href=\"");
					if (start != -1) {
						Movie movie = new Movie();
						int end = tarStr.indexOf("\" ");
						String link = tarStr.substring(start + 6, end);
						int startSrc = tarStr.indexOf("src=\"");
						int endSrc = tarStr.indexOf("\" />");
						String src = tarStr.substring(startSrc + 5, endSrc);
						int startName = tarStr.indexOf("m_name");
						int endName = tarStr.indexOf("</span>");
						String name = tarStr.substring(startName + 8, endName);
						movie.setLink(URLClass.MAIN_URL + link);
						movie.setSrc(URLClass.MAIN_URL + src);
						movie.setName(name);
						list.add(movie);
						tarStr = tarStr.substring(tarStr.indexOf(TAG5) + 5);
					} else {
						ret = false;
					}
				} else {
					ret = false;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				ret = false;
			}
		}
		return list;
	}

	//解析首页推荐电影列表
	public static ArrayList<Movie> parseMovieTab(String tarStr) {
		tarStr = parseHttpResultTJ(tarStr);
		ArrayList<Movie> list = new ArrayList<Movie>();
		boolean ret = true;
		while (ret) {
			try {
				if (tarStr.indexOf(TAG4) != -1) {
					int start = tarStr.indexOf("href=\"");
					if (start != -1) {
						Movie movie = new Movie();
						int end = tarStr.indexOf("\" ");
						String link = tarStr.substring(start + 6, end);
						int startSrc = tarStr.indexOf("src=\"");
						int endSrc = tarStr.indexOf("\" />");
						String src = tarStr.substring(startSrc + 5, endSrc);
						int startName = tarStr.indexOf("<span>");
						int endName = tarStr.indexOf("</span>");
						String name = tarStr.substring(startName + 6, endName);
						movie.setLink(URLClass.MAIN_URL + link);
						movie.setSrc(URLClass.MAIN_URL + src);
						movie.setName(name);
						list.add(movie);
						tarStr = tarStr.substring(tarStr.indexOf(TAG5) + 5);
					} else {
						ret = false;
					}
				} else {
					ret = false;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				ret = false;
			}
		}
		return list;
	}

	//解析电影，电视剧列表
	public static String parseHttpResultPlayIndex(String result) {
		String subStrUl = "";
		if (!TextUtils.isEmpty(result)) {
			int divIndex = result.indexOf(TAG6);
			if (divIndex != -1) {
				String subStrDiv = result.substring(divIndex);
				int ulEndIndex = subStrDiv.indexOf(TAG3);
				subStrUl = subStrDiv.substring(0, ulEndIndex);
			} else {
				int subPhoneUrlIndex = result.indexOf("title=\"手机版播放\"");
				if (subPhoneUrlIndex != -1) {
					String subPhoneUrl = result.substring(subPhoneUrlIndex);
					int ulEndIndex = subPhoneUrl.indexOf("target=_blank");
					subStrUl = subPhoneUrl.substring(20, ulEndIndex - 2);
				} else {
					return subStrUl;
				}
			}
		}
		return subStrUl;
	}

	//解析西瓜播放列表页
	public static ArrayList<PlayList> parsePlayIndex(String tarStr) {
		tarStr = parseHttpResultPlayIndex(tarStr);
		ArrayList<PlayList> list = new ArrayList<PlayList>();
		boolean ret = true;
		if (TextUtils.isEmpty(tarStr)) {
			return list;
		}
		int begin = tarStr.indexOf(TAG4);
		if (begin != -1) {
			tarStr = tarStr.substring(begin);
			while (ret) {
				try {
					int start = tarStr.indexOf("href=\"");
					if (start != -1) {
						PlayList play = new PlayList();
						int end = tarStr.indexOf("\" target");
						String link = tarStr.substring(start + 6, end);
						int startTitle = tarStr.indexOf("\"_blank\">");
						int endTitle = tarStr.indexOf("</a>");
						String title = tarStr.substring(startTitle + 9, endTitle);
						play.setLink(URLClass.PLAY_URL + link);
						play.setTitle(title);
						list.add(play);
						tarStr = tarStr.substring(tarStr.indexOf(TAG5) + 5);
					} else {
						ret = false;
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					ret = false;
				}
			}
		} else {
			PlayList play = new PlayList();
			play.setTitle("立刻观看");
			play.setLink(URLClass.XG_URL + tarStr);
			list.add(play);
		}
		return list;
	}

	//解析playUrl
	public static String parsePlayUrl(String tarStr) {
		String playUrl = "";
		if (!TextUtils.isEmpty(tarStr)) {
			int divIndex = tarStr.indexOf(TAG7);
			if (divIndex != -1) {//普通片
				String subStrDiv = tarStr.substring(divIndex);
				int startIndex = subStrDiv.indexOf(TAG8);
				int endIndex = subStrDiv.indexOf(TAG9);
				String subString = subStrDiv.substring(startIndex, endIndex);
				int start = subString.indexOf("\"");
				int end = subString.indexOf("\";url_n");
				playUrl = subString.substring(start + 1, end);
			} else {//福利片
				int urlIndex = tarStr.indexOf("'Url'");
				String subStr = tarStr.substring(urlIndex);
				int startIndex = subStr.indexOf("ftp");
				int endIndex = subStr.indexOf("\",");
				playUrl = subStr.substring(startIndex, endIndex);
			}
		}
		return playUrl;
	}


	//解析西瓜
	//解析首页推荐电影列表
	public static ArrayList<Movie> parseXG(String tarStr) {
		ArrayList<Movie> list = new ArrayList<Movie>();
		int index = tarStr.indexOf("box movie_list");
		if (index != -1) {
			tarStr = tarStr.substring(index);
			int ulEndIndex = tarStr.indexOf(TAG3);
			if (ulEndIndex != -1) {
				tarStr = tarStr.substring(0, ulEndIndex);
			} else {
				return list;
			}
		} else {
			return list;
		}
		boolean ret = true;
		while (ret) {
			try {
				if (tarStr.indexOf(TAG4) != -1) {
					int start = tarStr.indexOf("href=\"");
					if (start != -1) {
						Movie movie = new Movie();
						int end = tarStr.indexOf("\" ");
						String link = tarStr.substring(start + 6, end);
						int startSrc = tarStr.indexOf("src=\"");
						int endSrc = tarStr.indexOf("\" />");
						String src = tarStr.substring(startSrc + 5, endSrc);
						int startName = tarStr.indexOf("<h3>");
						int endName = tarStr.indexOf("</h3>");
						String name = tarStr.substring(startName + 4, endName);
						movie.setLink(URLClass.XG_URL + link);
						movie.setSrc(src);
						movie.setName(name);
						list.add(movie);
						tarStr = tarStr.substring(tarStr.indexOf(TAG5) + 5);
					} else {
						ret = false;
					}
				} else {
					ret = false;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				ret = false;
			}
		}
		return list;
	}

	//解析先锋
	public static ArrayList<Movie> parseXF(String tarStr) {
		ArrayList<Movie> list = new ArrayList<Movie>();
		int index = tarStr.indexOf("class=\"box_con\"");
		if (index != -1) {
			tarStr = tarStr.substring(index);
			int ulEndIndex = tarStr.indexOf(TAG3);
			if (ulEndIndex != -1) {
				tarStr = tarStr.substring(0, ulEndIndex);
			} else {
				return list;
			}
		} else {
			return list;
		}
		boolean ret = true;
		while (ret) {
			try {
				if (tarStr.indexOf(TAG4) != -1) {
					int start = tarStr.indexOf("href=\"");
					if (start != -1) {
						Movie movie = new Movie();
						int end = tarStr.indexOf("\" target");
						String link = tarStr.substring(start + 6, end);
						int startSrc = tarStr.indexOf("src=\"");
						int endSrc = tarStr.indexOf("\" title");
						String src = tarStr.substring(startSrc + 5, endSrc);
						int startName = tarStr.indexOf("title=\"");
						int endName = tarStr.indexOf("\" href");
						String name = tarStr.substring(startName + 7, endName);
						movie.setLink(URLClass.XF_URL + link);
						movie.setSrc(src);
						movie.setName(name);
						list.add(movie);
						tarStr = tarStr.substring(tarStr.indexOf(TAG5) + 5);
					} else {
						ret = false;
					}
				} else {
					ret = false;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				ret = false;
			}
		}
		return list;
	}

	//解析先锋播放列表页
	public static ArrayList<PlayList> parseXFPlayIndex(String tarStr) {
		ArrayList<PlayList> list = new ArrayList<PlayList>();
		int startIndex = tarStr.indexOf("xfplay高清播放");
		String subStr = tarStr.substring(startIndex);
		int endIndex = subStr.indexOf("' target");
		String urlLink = subStr.substring(subStr.indexOf("href='") + 6, endIndex);
		PlayList play = new PlayList();
		play.setTitle("立刻观看");
		play.setLink(URLClass.XF_URL + urlLink);
		list.add(play);
		return list;
	}

	//解析先锋playUrl
	public static String parseXFPlayUrl(String tarStr) {
		String playUrl = "";
		try {
			if (!TextUtils.isEmpty(tarStr)) {
				int divIndex = tarStr.indexOf("xfplay:");
				if (divIndex != -1) {
					tarStr = tarStr.substring(divIndex);
					int mzIndex = tarStr.indexOf("mz=");
					if (mzIndex != -1) {
						String middleStr = tarStr.substring(0, mzIndex + 3);
						String endStr = tarStr.substring(mzIndex+3);
						int endIndex = endStr.indexOf("|");
						if(endIndex!=-1){
							playUrl = middleStr+endStr.substring(0,endIndex);
						}
					}
				}
			}
		}catch (Exception e){
			playUrl = "";
		}
		return playUrl;
	}
}
