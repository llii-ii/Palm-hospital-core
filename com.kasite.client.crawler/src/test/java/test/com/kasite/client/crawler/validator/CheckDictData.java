package test.com.kasite.client.crawler.validator;

import com.kasite.core.common.validator.ValidatorUtils;
import com.kasite.core.common.validator.group.AddGroup;

public class CheckDictData {

	public static void main(String[] args) throws Exception {
		DemoEntity entity = new DemoEntity();
		entity.setDictA("016");
		ValidatorUtils.validateEntity(entity,AddGroup.class);
		System.exit(-1);
	}

}
