package hrbust.sprider.util;

import hrbust.sprider.domain.Page;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupUtil {
	// 获取所有url
	public List<String> getUrls(Page page) {
		List<String> list = new ArrayList<String>();
		Document doc = Jsoup.parse(page.getContent(), page.getUrl());
		Elements elements = doc.select("a[href]");
		for (Element element : elements) {
			String href = element.attr("href");
			if (href == null || href.trim().equals("")
					|| href.trim().equals("#")
					|| href.trim().toLowerCase().contains("javascript:"))
				continue;
			
			if (!href.startsWith("http")) {
				href = "http:" + href;
			}
			list.add(href);
		}
		return list;
	}

}
