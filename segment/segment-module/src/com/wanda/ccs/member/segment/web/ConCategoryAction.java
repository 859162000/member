package com.wanda.ccs.member.segment.web;

import java.util.ArrayList;
import java.util.List;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.member.segment.SegmentConstants;
import com.wanda.ccs.member.segment.service.ConCategoryService;
import com.wanda.ccs.member.segment.vo.ConCategoryVo;

/**
 * 注意这个树中过滤掉了原材料！
 * @author clzhang
 *
 */
public class ConCategoryAction {
	
	@InstanceIn(path="ConCategoryService")
	private ConCategoryService categoryService;

	private Long id = 0L;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<TreeNode> getSubCategories() {
		List<ConCategoryVo> categories = categoryService.getSubCategories(id);
		return convertTreeNodes(categories);
	}
	
	
	public List<TreeNode> getAllCategories() {
		List<ConCategoryVo> categories = categoryService.getAllCategories();
		return convertTreeNodes(categories);
	}
	
	
	private List<TreeNode> convertTreeNodes(List<ConCategoryVo> categories) {
		if(categories != null && categories.size() > 0) {
			int listSize = categories.size();
			List<TreeNode> treeNodes = new ArrayList<TreeNode>(listSize);
			for(int i=0 ; i<listSize ; i++) {
				ConCategoryVo cat = categories.get(i);
				if( SegmentConstants.MARTERIAL_TYPE.equals(cat.getItemType()) == false) {
					treeNodes.add(new TreeNode(cat));
				}
			}
			return treeNodes;
		}
		else {
			return null;
		}
	}
	

	/**
	 * 
	 * To be compatible with zTree note json format, parameters of this class is the same name for JSon format required for zTree. 
	 * 
	 * @author Charlie Zhang
	 *
	 */
	public static class TreeNode {
		
		String id;
		String pid;
		String name;
		boolean open = true;

		public TreeNode(ConCategoryVo vo) {
			this.id = Long.toString(vo.getConCategoryId());
			this.name = vo.getCategoryName();
			if(vo.getPConCategoryId() != null) {
				this.pid = Long.toString(vo.getPConCategoryId());
			}
		}
		
		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getPid() {
			return pid;
		}

		public boolean isOpen() {
			return open;
		}

	}
}
