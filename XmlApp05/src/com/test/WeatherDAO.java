/*=============================================================================
  WeatherDAO.java
  - DAO 구성
  - XML DOM 활용 → 원격 XML 읽어내기
    (https://www.weather.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=108)
=============================================================================*/

package com.test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WeatherDAO
{
	// 공통 멤버 구성 → 멤버 변수 → 초기화 → 생성자
	private Document xmlObj;
	private XPath xPath;
	private HashMap<String, String> map;		// 아이콘 연

	// 생성자 정의 → 기본 생성자
	public WeatherDAO() throws ParserConfigurationException, IOException, SAXException
	{
		this("108");	// 전국 기준
		/*
		stnId=108	전국
		stnId=109	서울,경기
		stnId=105	강원
		stnId=131	충북
		stnId=133	충남
		stnId=146	전북
		stnId=156	전남
		stnId=143	경북
		stnId=159	경남
		stnId=184	제주특별자치도
		*/
	}
	
	// 생성자 정의 → 매개변수 있는 생성자
	public WeatherDAO(String stnId) throws ParserConfigurationException, IOException, SAXException
	{
		map = new HashMap<String, String>();
		map.put("맑음", "NB01.png");
		map.put("구름조금", "NB02.png");
		map.put("구름많음", "NB03.png");
		map.put("흐림", "NB04.png");
		map.put("비", "NB05.png");
		map.put("흐리고 비", "NB08.png");
		map.put("눈", "NB11.png");		
		map.put("구름많고 비", "NB20.png");
		map.put("구름많고 눈", "NB21.png");
		map.put("흐리고 눈", "NB21.png");
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		String str = String.format
				("https://www.weather.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=%s"
						, stnId);
		URL url = new URL(str);
		InputSource is = new InputSource(url.openStream());
		
		xmlObj = builder.parse(is);
		xPath = XPathFactory.newInstance().newXPath();
		//-- XPathFactory xFactory = XPathFactory.newInstance();
		//   xPath = xFactory.newXPath();
		//   와 동일한 구문
		
		/*
		○ XPath 생성
		   - XPathFactory 의 정적 메소드(static) 『newInstance()』 호출을 통해
		     XPath 를 생성해주는 XPathFactory 를 생성하고
		   - 이 XPathFactory 의 정적 메소드 『newXPath()』 호출을 통해
		     XPath 객체를 생성한다.
		
		○ 노드 선택(Selecting Nodes)
		   - 브라우저마다 XPath 를 처리하는 방법이 다르다.
		   - Chrome, Firefox, Edge, Opera, Safari 는
		     『evaluation()』 메소드를 사용하여 노드를 처리한다.
		     → xmlDoc.evaluate(xpath, xmlDoc, null, XPathResult.ANY_TYPE, null)
		   - IE 는 『selectNodes()』 메소드를 사용하여 노드를 선택한다.
		   
		○ XPath 의 『compile(XPath 경로 표현식)』
		   - XML 데이터 Parsing
		     1. XML 이 제공되는 URL 로 접속하여 데이터를 수신한다.
		     2. DocumentBuilderFactory ... newInstance() 로 factory 를 생성한다.
		     3. DocumentBuilder ... newDocumentBuilder() 로 builder 를 생성한다.
		     4. InputSource is ... new InputSource() 로 InputSource 를 생성한다.
		        이 때, 파일로 수신한 경우라면 File 객체를 넘겨준다.
		     5. Document xmlObj = builder.parse(is) 로 XML 을 파싱한다.
		     6. XPath xPath = XPathFactory.newInstance().newXPath() 로
		        XPath 객체를 생성하고
		     7. XPathExpression expr = XPath.compile( XPath 경로 표현식 ) 으로
		        가져올 Element 를 선택한다.
		     8. 해당 노드(Element)에 접근하여 필요한 데이터를 추출한다.     
		     
		*/
	}
	
	// <title>서울,경기도 육상 중기예보 - 2020년 06월 10일 (수)요일 06:00 발표</title>
	public String weatherTitle() throws XPathExpressionException
	{
		String result = "";
		
		result = xPath.compile("/rss/channel/item/title").evaluate(xmlObj);
		
		return result;
	}
	
	/*
	<wf>
	<![CDATA[ 
	○ (강수) 13일(토)은 오후에 비가 오겠습니다.<br />○ (기온) 13일(토)~20일(토) 낮 기온은 24~30도로 어제(9일, 29~36도)보다 낮겠습니다.<br />○ (해상) 서해중부해상 물결은 1.0~2.0m로 일겠습니다.<br />○ (주말전망) 13일(토) 흐리고 비가 오겠고, 14일(일) 오전에는 흐리다가 오후에 맑겠습니다. 아침 기온은 19~20도, 낮 기온은 24~28도의 분포가 되겠습니다.<br /><br />* 13일(토)은 저기압의 영향으로 서울.인천.경기도까지 비가 오겠으나, 15일(월) 이후에는 정체전선이 제주도남쪽먼바다에서 남북으로 오르내리면서 날씨 변동성이 커지겠으니, 앞으로 발표되는 기상정보를 참고하기 바랍니다. 
	]]>
	</wf>
	*/	
	public String weatherInfo() throws XPathExpressionException
	{
		String result = "";
		
		result = xPath.compile("/rss/channel/item/description/header/wf").evaluate(xmlObj);
		
		return result;
	}
	
	/*
	○ XPath 의 『evaluate()』 메소드 두 번째 파라미터
	   - XPathConstants.NODESET
	   - XPathConstants.NODE
	   - XPathConstants.BOOLEAN
	   - XPathConstants.NUMBER
	   - XPathConstants.STRING
	*/
	
	// 도시 이름 배열 구성
	public ArrayList<String> weatherCityList() throws XPathExpressionException
	{
		ArrayList<String> result = new ArrayList<String>();
		
		NodeList cityNodeList 
			= (NodeList)xPath
				.compile("/rss/channel/item/description/body/location/city")
				.evaluate(xmlObj, XPathConstants.NODESET);
		
		for(int a=0; a<cityNodeList.getLength(); a++)
		{
			Node cityNode = cityNodeList.item(a);
			result.add(cityNode.getTextContent());
		}
		
		return result;
	}
	
	
	// 날씨 정보 리스트
	public ArrayList<WeatherDTO> weatherList(String idx) throws XPathExpressionException
	{
		ArrayList<WeatherDTO> result = new ArrayList<WeatherDTO>();
		
		NodeList dataNodeList 
			= (NodeList)xPath
			.compile(String.format("/rss/channel/item/description/body/location[%s]/data", idx))
			.evaluate(xmlObj, XPathConstants.NODESET);
		
		for(int a=1; a<=dataNodeList.getLength(); a++)
		{
			// tmEf
			String tmEf 
				= xPath.compile(
					String.format(
					"/rss/channel/item/description/body/location[%s]/data[%s]/tmEf"
					, idx, a))
				.evaluate(xmlObj);
			
			// wf
			String wf 
			= xPath.compile(
				String.format(
				"/rss/channel/item/description/body/location[%s]/data[%s]/wf"
				, idx, a))
			.evaluate(xmlObj);
			
			// tmn
			String tmn
			= xPath.compile(
				String.format(
				"/rss/channel/item/description/body/location[%s]/data[%s]/tmn"
				, idx, a))
			.evaluate(xmlObj);
			
			// tmx
			String tmx
			= xPath.compile(
				String.format(
				"/rss/channel/item/description/body/location[%s]/data[%s]/tmx"
				, idx, a))
			.evaluate(xmlObj);
			
			// rnSt
			String rnSt
			= xPath.compile(
				String.format(
				"/rss/channel/item/description/body/location[%s]/data[%s]/rnSt"
				, idx, a))
			.evaluate(xmlObj);
			
			WeatherDTO w = new WeatherDTO();
			w.setTmEf(tmEf);
			w.setWf(wf);
			w.setTmn(tmn);
			w.setTmx(tmx);
			w.setRnSt(rnSt);
			w.setImg(map.get(wf));
			
			result.add(w);
			
		}
		
		return result;
		
	}
	
	

}





















