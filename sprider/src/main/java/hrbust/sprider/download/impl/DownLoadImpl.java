package hrbust.sprider.download.impl;

import hrbust.sprider.domain.Page;
import hrbust.sprider.download.DownLoad;
import hrbust.sprider.util.HttpUtils;

public class DownLoadImpl implements DownLoad{

	@Override
	public Page downLoadPage(String url) {
		HttpUtils httpUtils = HttpUtils.getInstance();
		String content = httpUtils.get(url);
		Page page = new Page();
		page.setUrl(url);
		page.setContent(content);
		return page;
	}
	
}
