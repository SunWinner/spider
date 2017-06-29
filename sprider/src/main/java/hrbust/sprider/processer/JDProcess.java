package hrbust.sprider.processer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import hrbust.sprider.domain.Page;

public class JDProcess implements Processable{

	@Override
	public void process(Page page) {
		Document doc = Jsoup.parse(page.getContent());
		
		
	}
	
}
