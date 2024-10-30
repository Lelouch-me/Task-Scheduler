package com.kkr.app.service.impl;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kkr.app.service.INewspaperWebsiteScrappingService;
import com.kkr.common.util.CommonUtils;

public class NewspaperWebsiteScrappingService implements INewspaperWebsiteScrappingService{

	public static final String TBSInternationalNewsURL = "https://www.tbsnews.net/world/global-economy";
	public static final String TBSMarketURL = "https://www.tbsnews.net/economy/stocks";
	public static final String TBSEconomyURL = "https://www.tbsnews.net/economy";
	public static final String TFEEconomyBangladeshURL = "https://thefinancialexpress.com.bd/page/economy/bangladesh";
	public static final String TFEEconomyGlobalURL = "https://thefinancialexpress.com.bd/page/economy/global";
	public static final String TFEStockBangladeshURL = "https://thefinancialexpress.com.bd/page/stock/bangladesh";
	public static final String TFEStockGlobalURL = "https://thefinancialexpress.com.bd/page/stock/global";
	public static final String TDSEconomyBangladeshURL = "https://www.thedailystar.net/business/economy";
	public static final String TDSEconomyGlobalURL = "https://www.thedailystar.net/business/global-economy";
	
	
	public static final String tbsURLArray[] = {TBSMarketURL,TBSEconomyURL,TBSInternationalNewsURL};
	public static final String tfeURLArray[] = {TFEEconomyBangladeshURL,TFEEconomyGlobalURL,TFEStockBangladeshURL,TFEStockGlobalURL};
	public static final String tdsURLArray[] = {TDSEconomyBangladeshURL,TDSEconomyGlobalURL};
	
	public static final String insert_query = "insert ignore into news(title,link,thumbnail_url,type,pub_date,date_time) values(?,?,?,?,?,NOW())";
//	public static PreparedStatement pst = null;
	public static PreparedStatement pstProd = null;
//	public static Connection con = null;
	public static Connection conProd = null;
	
	@Override
	public void scrappAllNewsWebsite() {
		try {
			
//			if(!InetAddress.getByName("192.168.0.15").isReachable(10000)) 
//				CommonUtils.sendMail(null, "Bloomberg PC is off. Please turn it on.", "Bloomberg PC Status");
				
//	    	con = CommonUtils.connectMainDB();
	    	conProd = CommonUtils.connectDC();
//	    	pst = con.prepareStatement(insert_query);
	    	pstProd = conProd.prepareStatement(insert_query);
	    	
	    	scrappeDataFromTFEWebsite();
	    	scrappeDataFromTDSWebsite();		    		    
		    scrappeDataFromTBSWebsite();
		    
//		    CommonUtils.truncateTable(con,"news");
		    CommonUtils.truncateTable(conProd,"news");
		    
//		    pst.executeBatch();
		    pstProd.executeBatch();
		    
//		    con.close();
		    conProd.close();
		    
	    }catch(Exception e) {
		    e.printStackTrace();
	    }
	}
	
	private static void scrappeDataFromTDSWebsite() {
		try {
			for(String tdsURL : tdsURLArray) {
				Document doc = null;
				try {
					doc = Jsoup.connect(tdsURL).maxBodySize(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
							.ignoreContentType(true).maxBodySize(0).timeout(20 * 1000).get();
					
					Elements tabs = doc.select(".card.position-relative");		
					for(Element tab : tabs) {
						//Element firstDiv = tab.select(".card-image.bg-light-gray.position-relative").get(0);	
						Element firstDiv = null;
						Elements firstDivs = tab.select(".card-image.bg-light-gray.position-relative");
						if(firstDivs.size()==0) {
							firstDiv = tab.select(".card.position-relative.border-bottom").get(0);
						}else {
							firstDiv = tab.select(".card-image.bg-light-gray.position-relative").get(0);
						}
						Element imgSource = firstDiv.select("img").get(0);
						String is = imgSource.attr("data-src");
						if(is.isEmpty() || is.equals("") || !(is!=null)) {
							is = imgSource.attr("data-srcset");
						}
						
						Element secondDiv = tab.getElementsByClass("card-content").get(0);
						Element titleArea = secondDiv.select("a").get(0);
						String title = titleArea.text();
						String link = titleArea.attr("href");
						link = "https://www.thedailystar.net" + link;
						String pubDate = null;
						try {
							Document pub_doc = Jsoup.connect(link).maxBodySize(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
									.ignoreContentType(true).maxBodySize(0).timeout(20 * 1000).get();
							
							Element dateTab = pub_doc.select(".date.text-14").get(0);
							String date = dateTab.text();
							pubDate = date.split("on: ")[1];
							Date publishedDate = new SimpleDateFormat("EEE MMM dd, yyyy HH:mm aa", Locale.ENGLISH).parse(pubDate);
							LocalDate ld = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(publishedDate));
							LocalDate d = LocalDate.now();
							d = d.minusDays(2);
							pubDate = ld.isBefore(d) ? "Old" : new SimpleDateFormat("MMMM dd, yyyy hh:mm aa").format(publishedDate);
							if(pubDate.equals("Old")) continue;
						}catch(Exception e) {
							if(!e.getMessage().contains("Read") && !e.getMessage().contains("HTTP")) {
								System.out.println("Exception occurred while getting published date for daily star due to "+e);
							}
						}
						
						insertIntoNewsTable(title,link,is,tdsURL,pubDate);
					}					
				}catch(Exception e) {
					System.out.println(e);
				}
			}			
		}catch(Exception e) {
			System.out.println(e);
		}
	}

//	private static void scrappeDataFromTFEWebsite() {
//		try {
//			for(String tfeURL : tfeURLArray) {
//				Document doc = null;
//				try {
//					doc = Jsoup.connect(tfeURL).maxBodySize(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//							.ignoreContentType(true).maxBodySize(0).timeout(20 * 1000).get();
//					
//					Elements tabs = doc.select("article");		
//					
//					for(int i = 0; i<tabs.size();i++) {
//						Element tab = tabs.get(2);
//						Element firstDiv = tab.getElementsByClass("col-auto xl:col-span-10").get(1);				
//						Elements imgSource = firstDiv.select("figure");
//						String is = imgSource.attr("data-src");
//						
//						Element secondDiv = tab.getElementsByClass("font-semibold").get(0);
//						Element titleArea = secondDiv.select("a").get(0);
//						String title = titleArea.text();
//						String link = titleArea.attr("href");
//						link="https://thefinancialexpress.com.bd"+link;
//						String pubDate = null;
//						
//						try {
//							Document pub_doc = Jsoup.connect(link).maxBodySize(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//									.ignoreContentType(true).maxBodySize(0).timeout(20 * 1000).get();
//							
//							Elements dateTabMain = pub_doc.select("time");
//							Element dateTab = null;
//							if(dateTabMain.size()>1) {
//								dateTab = dateTabMain.get(1);
//							}else {
//								dateTab = dateTabMain.get(0);
//							}
//							String date = dateTab.text();
//							date = date.equals("") ? dateTabMain.get(1).text() : date;
//							//pubDate = date.contains("Updated: ") ? date.split("Updated: ")[1] : date.split("Published: ")[1];
//							Date publishedDate = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa", Locale.ENGLISH).parse(date);
//							LocalDate ld = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(publishedDate));
//							LocalDate d = LocalDate.now();
//							d = d.minusDays(2);
//							pubDate = ld.isBefore(d) ? "Old" : new SimpleDateFormat("MMMM dd, yyyy hh:mm aa").format(publishedDate);
//							if(pubDate.equals("Old")) continue;
//						}catch(Exception e) {
//							if(!e.getMessage().contains("Read") && !e.getMessage().contains("HTTP")) {
//								System.out.println("Exception occurred while getting published date for "+ link+" due to "+e);
//							}
//						}
//						
//						insertIntoNewsTable(title,link,is,tfeURL,pubDate);
//					}					
//				}catch(Exception e) {
//					System.out.println(e);
//				}
//			}			
//		}catch(Exception e) {
//			System.out.println(e);
//		}
//	}
	
	
	private static void scrappeDataFromTFEWebsite() {
		try {
			for(String tfeURL : tfeURLArray) {
				Document doc = null;
				try {
					doc = Jsoup.connect(tfeURL).maxBodySize(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
							.ignoreContentType(true).maxBodySize(0).timeout(20 * 1000).get();
					
					Elements tabs = doc.select("article");		
					
					for(Element link : tabs) {
						Elements news = link.select("a");
						for(Element newsDetails : news ) {
							String href = newsDetails.attr("href");
							if( !href.equals("/page/economy/bangladesh") && !href.equals("/page/economy/global")) {
								Document doc_news = Jsoup.connect("https://thefinancialexpress.com.bd"+href).get();
								
								Elements fig = doc_news.select("figure");
								Elements img = fig.select("img");
								
								Elements time = doc_news.select("time");
								Elements title = doc_news.select("h1");
								
								for(Element pubTime : time) {
									String timeString = pubTime.text();
							        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
							        Date oldDate = format.parse(timeString);
							        Date date = DateUtils.addHours(oldDate, 6);
							        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
							        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
							        String dateStr = dateFormatter.format(date);
							        String timeStr = timeFormatter.format(date);
							        
							        String newsTitle=title.text();
							        String thumbnailLink=img.attr("srcset");
							        String newsDate=dateStr + " " + timeStr;
							        String newsLink="https://thefinancialexpress.com.bd"+href;
							        Date publishedDate = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa", Locale.ENGLISH).parse(newsDate);
							        LocalDate ld = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(publishedDate));
									LocalDate d = LocalDate.now();
									d = d.minusDays(2);
									newsDate = ld.isBefore(d) ? "Old" : new SimpleDateFormat("MMMM dd, yyyy hh:mm aa").format(publishedDate);
									if(newsDate.equals("Old")) continue;
							        if(thumbnailLink !=null && !thumbnailLink.equals("")) {
							        insertIntoNewsTable(newsTitle,newsLink,thumbnailLink,tfeURL,newsDate);
//							        System.out.println(newsTitle);
//							        System.out.println(newsLink);
//							        System.out.println(thumbnailLink);
//							        System.out.println(newsDate);
							        }
							       break;
								}
							}
						}
					}					
				}catch(Exception e) {
					System.out.println(e);
				}
			}			
		}catch(Exception e) {
			System.out.println(e);
		}
	}

	private static void scrappeDataFromTBSWebsite() {
		try {
			for(String tbsURL : tbsURLArray) {
				Document doc = null;
				try {
					doc = Jsoup.connect(tbsURL).maxBodySize(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
							.ignoreContentType(true).maxBodySize(0).timeout(20 * 1000).get();
					
					Elements tabs = doc.select(".card.relative");		
					
					for(Element tab : tabs) {
						Element firstDiv = tab.select(".card-image.relative").get(0);				
						Element linkSource = firstDiv.select("a").get(0);
						Element imgSource = linkSource.select("img").get(0);
						String is = imgSource.attr("data-src");
						String link = linkSource.attr("href");
						
						Element secondDiv = tab.getElementsByClass("card-section").get(0);
						Element titleArea = secondDiv.select("a").get(1);
						String title = titleArea.text();
						link = "https://www.tbsnews.net" + link;
						String pubDate = null;
						
						try {
							Document pub_doc = Jsoup.connect(link).maxBodySize(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
									.ignoreContentType(true).maxBodySize(0).timeout(20 * 1000).get();
							
							Element dateTab = pub_doc.getElementsByClass("date").get(3);
							String date = dateTab.text();
							pubDate = date.split("modified: ")[1];
							Date publishedDate = new SimpleDateFormat("dd MMMM, yyyy, HH:mm aa", Locale.ENGLISH).parse(pubDate);
							LocalDate ld = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(publishedDate));
							LocalDate d = LocalDate.now();
							d = d.minusDays(2);
							pubDate = ld.isBefore(d) ? "Old" : new SimpleDateFormat("MMMM dd, yyyy hh:mm aa").format(publishedDate);
							if(pubDate.equals("Old")) continue;
						}catch(Exception e) {
							if(!e.getMessage().contains("Read") && !e.getMessage().contains("HTTP")) {
								System.out.println("Exception occurred while getting published date for business standard due to "+e);
							}
						}					
						insertIntoNewsTable(title,link,is,tbsURL,pubDate);
					}
				}catch(Exception e) {
					System.out.println(e);
				}
			}	
		}catch(Exception e) {
			System.out.println(e);
		}
	}

	private static void insertIntoNewsTable(String title, String link, String is,String URL,String pubDate) {
		try {
//			pst.setString(1,title);
//			pst.setString(2,link);
//			pst.setString(3,is);
//			
//			if(URL.equals(TBSMarketURL) || URL.equals(TFEStockBangladeshURL)) {
//          	  pst.setString(4,"Market");
//            }else if(URL.equals(TBSInternationalNewsURL) || URL.equals(TFEEconomyGlobalURL) || URL.equals(TFEStockGlobalURL) 
//            		|| URL.equals(TDSEconomyGlobalURL)){
//          	  pst.setString(4,"International");
//            }else {
//          	  pst.setString(4,"Economy");
//            }			
//			if(pubDate!=null && !pubDate.equals("")) {
//				pst.setString(5,pubDate);
//			}else {
//				pst.setNull(5,java.sql.Types.NULL);
//			}
//			
//			pst.addBatch();
			
			
			pstProd.setString(1,title);
			pstProd.setString(2,link);
			pstProd.setString(3,is);
			
			if(URL.equals(TBSMarketURL) || URL.equals(TFEStockBangladeshURL)) {
				pstProd.setString(4,"Market");
            }else if(URL.equals(TBSInternationalNewsURL) || URL.equals(TFEEconomyGlobalURL) || URL.equals(TFEStockGlobalURL) 
            		|| URL.equals(TDSEconomyGlobalURL)){
            	pstProd.setString(4,"International");
            }else {
            	pstProd.setString(4,"Economy");
            }
			
			if(pubDate!=null && !pubDate.equals("")) {
				pstProd.setString(5,pubDate);
			}else {
				pstProd.setNull(5,java.sql.Types.NULL);
			}
			
			pstProd.addBatch();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		new NewspaperWebsiteScrappingService().scrappAllNewsWebsite();
	}
}
