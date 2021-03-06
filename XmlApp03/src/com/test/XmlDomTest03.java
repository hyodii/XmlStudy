/*===================================================
	XmlDomTest03.java
	- 콘솔 기반 자바 프로그램
	- XML DOM 활용 → 로컬 (local) XML 읽어내기
	  (breakfast_menu.xml)
===================================================*/

// breakfast_menu.xml 파일을 대상으로
/*
■ [Belgian Waffles] $5.95 650칼로리
- Two of our famous Belgian Waffles with plenty of real maple syrup
----------------------------------------------------------------------------------------
■ [Strawberry Belgian Waffles] $7.95 900칼로리
- Light Belgian waffles covered with strawberries and whipped cream
----------------------------------------------------------------------------------------
■ [Berry-Berry Belgian Waffles] $8.95 900칼로리
- Light Belgian waffles covered with an assortment of fresh berries and whipped cream
----------------------------------------------------------------------------------------
				:
*/

// 이와 같이 결과 출력이 이루어질 수 있도록 프로그램을 작성한다.

package com.test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDomTest03
{
	public static void main(String[] args)
	{
		// 1. XML 파일을 메모리에 로드 → XML DOM 형성
		// 2. 루트 엘리먼트 접근
		// 3. 특정 하위 엘리먼트 접근 → 위치, 이름 등을 기준으로 접근
		// 4. 텍스트 노드(속성 노드) 접근 → 데이터 획득
		// 5. 결과 출력
		
		try
		{
			// XML 파일을 메모리에 로드시킬 준비
			// → XML DOM 형성을 위한 준비
			// (이를 위해 필요한 리소스 구성)
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlObj = null;
			
			// XML 파일을 메모리에 로드 → XML DOM 형성
			String url = "breakfast_menu.xml";
			xmlObj = builder.parse(url);
			
			// 루트 엘리먼트 접근 (도큐먼트엘리먼트 == 루트 엘리먼트)
			Element root = xmlObj.getDocumentElement();
			
			// 테스트
			System.out.println(root.getNodeName());
			//--==>> memberList
			
			// 특정 하위 엘리먼트 접근 → 위치, 이름을 기준으로 접근
			// 『getElementsByTagName()』 메소드는 태그의 이름을 가지고
			// 자식(자손) 노드에 접근하는 메소드
			NodeList foodNodeList = root.getElementsByTagName("food");
			
			// 이렇게 얻어낸 NodeList 객체에 포함되어 있는 Node 의 갯수를
			// 『getLength()』메소드를 통해 확인할 수 있다.
			
			for (int i = 0; i < foodNodeList.getLength(); i++)	// 0 ~ 1
			{
				Node foodNode = foodNodeList.item(i);
				//-- 『getElementsByTagName()』 메소드가 이름을 통해 대상을 획득했다면,
				//   『item()』 메소드는 위치(인덱스)를 통해 대상을 획득하게 된다.
				
				// 캐스팅
				Element foodElement = (Element)foodNode;
				
				
				System.out.printf("■[%s] %s %s칼로리%n - %s%n"
						, getText(foodElement, "name")
						, getText(foodElement, "price")
						, getText(foodElement, "calories")
						, getText(foodElement, "description"));
				System.out.println("---------------------------------------------------------------------------------------");
				
				// 커리큘럼 추가 -------------------------------------------------
				
				
			}
			
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
	}
	
	public static String getText(Element parent, String tagName)
	{
		// 반환할 결과 값
		String result = "";
		
		// 특정 태그 이름을 가진 객체의 첫 번째 자식 노드 얻어온 다음
		Node node = parent.getElementsByTagName(tagName).item(0);
		Element element = (Element)node;
		
		// 특정 엘리먼트의 자식 노드(Text Node)의 값(nodeValue)을 얻어올 수 있도록 처리
		result = element.getChildNodes().item(0).getNodeValue();
		
		// 결과값 반환
		return result;
	}
}
