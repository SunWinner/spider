package hrbust.sprider.domain;

import java.util.List;
import java.util.Map;

public class Page {
	//本页的url
	private String url;
	//本页正文
	private String content;
	//本页所提取的内容
	private Map<Object,Object> value;
	//本页所包含的url
	private List<String> urlList;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Map<Object, Object> getValue() {
		return value;
	}
	public void setValue(Map<Object, Object> value) {
		this.value = value;
	}
	public List<String> getUrlList() {
		return urlList;
	}
	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}
}
