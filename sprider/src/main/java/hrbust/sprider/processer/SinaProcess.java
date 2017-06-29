package hrbust.sprider.processer;

import hrbust.sprider.domain.Page;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SinaProcess implements Processable{

	@Override
	public void process(Page page) {
		Document doc = Jsoup.parse(page.getContent());
		Element title = doc.getElementById("main_title");
		Element content = doc.getElementById("artibody");
		Map<Object,Object> value = new HashMap<Object, Object>();
		value.put("title", title.text());
		value.put("content", content.text());
		page.setValue(value);
	}
}
