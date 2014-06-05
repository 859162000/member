package com.wanda.ccs.mbr.tag.service.impl;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wanda.ccs.mbr.tag.service.TagBuildService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/application*.xml")
public class TagBuildServiceImplTest {
	@Resource
	TagBuildService tagBuildService = null;
	@Test
	public void testBuildPriceSensitive() {
		try {
			tagBuildService.buildPriceSensitive();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testBuildFamilyComposition() {
		try {
			tagBuildService.buildFamilyComposition();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testAbnormalConsumption() {
		try {
			tagBuildService.buildAbnormalConsumption();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testMembersAcitveRate() {
		try {
			tagBuildService.buildMembersAcitveRate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testBuildEchannelPreference() {
		try {
			tagBuildService.buildEchannelPreference();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testFilmPreferences() {
		try {
			tagBuildService.buildFilmPreferences();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
