package com.wanda.member.basic.partitioner;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class LevelPartitioner implements Partitioner {
	
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		
		Map<String, ExecutionContext> results = new LinkedHashMap<String, ExecutionContext>();
		
		ExecutionContext contextLv2 = new ExecutionContext();
		contextLv2.put("level", 2);
		results.put("partition.mbrlevel."+2, contextLv2);
		
		ExecutionContext contextLv3 = new ExecutionContext();
		contextLv3.put("level", 3);
		results.put("partition.mbrlevel."+3, contextLv3);
		
		ExecutionContext contextLv4 = new ExecutionContext();
		contextLv4.put("level", 4);
		results.put("partition.mbrlevel."+4, contextLv4);
		return results;
	}

}
