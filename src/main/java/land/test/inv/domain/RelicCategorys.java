package land.test.inv.domain;

import java.util.ArrayList;
import java.util.List;

public class RelicCategorys {
	
	private Integer catgoryCode;
	private String catgoryName;
	private List<RelicCategorys> childNode = new ArrayList<RelicCategorys>();
	
	public Integer getCatgoryCode() {
		return catgoryCode;
	}
	
	public void setCatgoryCode(Integer catgoryCode) {
		this.catgoryCode = catgoryCode;
	}
	
	public String getCatgoryName() {
		return catgoryName;
	}

	public void setCatgoryName(String catgoryName) {
		this.catgoryName = catgoryName;
	}
	
	public void addChildNode(RelicCategorys node) {
		this.childNode.add(node);
	}

	public RelicCategorys getChildNode(int catgoryCode) {
		for (RelicCategorys node : this.childNode) {
			if (node.getCatgoryCode().intValue() == catgoryCode) {
				return node;
			}
		}
		return null;
	}
	
	public RelicCategorys getChildNode(String catgoryName) {
		for (RelicCategorys node : this.childNode) {
			if (node.getCatgoryName().equals(catgoryName)) {
				return node;
			}
		}
		return null;
	}
	
	public List<RelicCategorys> getChildNodes(){
		return this.childNode;
	}
	
	public boolean hasChildNodes() {
		return !this.childNode.isEmpty();
	}

}
