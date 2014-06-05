package com.wanda.ccs.mbr.tag.service.impl;

import org.springframework.stereotype.Service;
import com.wanda.ccs.mbr.tag.dao.MyBatisDAO;
import com.wanda.ccs.mbr.tag.service.TagBuildService;
@Service("tagBuildService")
public class TagBuildServiceImpl extends MyBatisDAO implements TagBuildService {

	@Override
	public void buildPriceSensitive() throws Exception {
		sqlSession.update("cleanPriceSensitive");		
		sqlSession.update("buildPriceSensitive");
	}

	@Override
	public void buildFamilyComposition() throws Exception {
		sqlSession.update("cleanFamilyComposition");		
		sqlSession.update("buildFamilyComposition");
	}

	@Override
	public void buildAbnormalConsumption() throws Exception {
//		sqlSession.update("cleanAbnormalConsumption");		
		sqlSession.update("buildAbnormalConsumption");
	}

	@Override
	public void buildMembersAcitveRate() throws Exception {
		sqlSession.update("cleanMembersAcitveRate");		
		sqlSession.update("buildMembersAcitveRate");
	}

	@Override
	public void buildEchannelPreference() throws Exception {
		sqlSession.update("cleanEchannelPreference");		
		sqlSession.update("buildEchannelPreference");
	}

	@Override
	public void buildFilmPreferences() throws Exception {
		sqlSession.update("cleanFilmPreferences");		
		sqlSession.update("buildFilmPreferences");
	}
}
