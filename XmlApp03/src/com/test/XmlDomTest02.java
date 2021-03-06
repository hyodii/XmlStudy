/*===================================================
	XmlDomTest02.java
	- 콘솔 기반 자바 프로그램
	- XML DOM 활용 → 로컬 (local) XML 읽어내기
	  (VEHICLES.xml)
===================================================*/

package com.test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDomTest02
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
			String url = "memberList.xml";
			xmlObj = builder.parse(url);
			
			// 루트 엘리먼트 접근 (도큐먼트엘리먼트 == 루트 엘리먼트)
			Element root = xmlObj.getDocumentElement();
			
			// 테스트
			System.out.println(root.getNodeName());
			//--==>> memberList
			
			// 특정 하위 엘리먼트 접근 → 위치, 이름을 기준으로 접근
			// 『getElementsByTagName()』 메소드는 태그의 이름을 가지고
			// 자식(자손) 노드에 접근하는 메소드
			NodeList memberNodeList = root.getElementsByTagName("memberInfo");
			
			// 이렇게 얻어낸 NodeList 객체에 포함되어 있는 Node 의 갯수를
			// 『getLength()』메소드를 통해 확인할 수 있다.
			
			// 테스트
			//System.out.println(memberNodeList.getLength());
			//--==>> 2
			
			for (int i = 0; i < memberNodeList.getLength(); i++)	// 0 ~ 1
			{
				Node memberNode = memberNodeList.item(i);
				//-- 『getElementsByTagName()』 메소드가 이름을 통해 대상을 획득했다면,
				//   『item()』 메소드는 위치(인덱스)를 통해 대상을 획득하게 된다.
				
				// 캐스팅
				Element memberElement = (Element)memberNode;
				
				
				System.out.printf("%s %s%n"
						, getText(memberElement, "name")
						, getText(memberElement, "telephone"));
				//--==>> 윤유동 010-2944-6341
				//		 박효빈 010-5541-7266
				
				// 커리큘럼 추가 -------------------------------------------------
				
				// memberElement 로 부터 curriculumn NodeList 얻어오기
				NodeList curriculumnNodeList = memberElement.getElementsByTagName("curriculumn");
				
				if (curriculumnNodeList.getLength() > 0)
				{
					Node curriculumnNode = curriculumnNodeList.item(0);
					Element curriculumnElement = (Element)curriculumnNode;
					
					// 방법 1.
					/*
					NodeList subNodeList = curriculumnElement.getElementsByTagName("sub");
					for(int m=0; m<subNodeList.getLength();m++)
					{
						Node subNode = subNodeList.item(0);
						Element subElements = (Element)subNode;
						System.out.printf("%s", subElements.getTextContent());
					}
					System.out.println();
					*/
					
					// 방법 2.
					/*
					----------------------------------------------
					Node Type	Named	Constent
					----------------------------------------------
					1			ELEMENT_NODE
					2			ATTRIBUTE_NODE
					3			TEXT-NODE
					4			CDATA_SELECTION_NODE
					5			ENTITY_REFERENCE_NODE
					6			ENTITY_NODE
					7			PROCESSING_INSTRUCTION_NODE
					8			COMMENT_NODE
					9			DOCUMENT_NODE
					10			DOCUMENT_TYPE_NODE
					11			DOCUMENT_FRAGMENT_NODE
					12			NOTATION_NODE
					----------------------------------------------
					*/
					
					NodeList subNodeList = curriculumnElement.getChildNodes();			// check~!!!
					for (int m = 0; m < subNodeList.getLength(); m++)
					{
						Node subNode = subNodeList.item(m);
						if(subNode.getNodeType() == 1)		// ELEMENT_NODE				// CHECK~!!!
						{
							Element subElement = (Element)subNode;
							System.out.printf("%s ", subElement.getTextContent());
						}
					}
					System.out.println();
				}
				
				
				// ------------------------------------------------- 커리큘럼 추가
				
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
