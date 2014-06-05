package com.wanda.ccs.model;

import java.util.ArrayList;
import java.util.Collection;

public class RegionCodeUtil {
	public static <T extends IRegionCode> T getByRegion(Collection<T> set,
			String region) {
		for (T def : set) {
			if (region == null || region.equals("")) {
				if (def.getRegionCode() == null
						|| def.getRegionCode().equals(""))
					return def;
			} else if (region.equals(def.getRegionCode()))
				return def;
		}

		return null;
	}

	public static <T extends IRegionCode> ArrayList<T> getAllByRegion(
			Collection<T> set, String region) throws Exception {
		ArrayList<T> vec = new ArrayList<T>();
		for (T def : set) {
			if (region == null || region.equals("")) {
				if (def.getRegionCode() == null
						|| def.getRegionCode().equals(""))
					vec.add(def);
			} else if (region.equals(def.getRegionCode()))
				vec.add(def);
		}

		return vec;
	}

}
