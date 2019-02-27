package test.com.kasite.client.crawler.validator;

import com.kasite.client.crawler.config.CheckDictBuser;
import com.kasite.core.common.validator.CheckDict;
import com.kasite.core.common.validator.group.AddGroup;

public class DemoEntity{
		@CheckDict(version="1.5", inf=CheckDictBuser.class,type="GB/T 2659-2000",isNotNull = true,message=" 字典值不能为空",groups=AddGroup.class)
		private String dictA;

		public String getDictA() {
			return dictA;
		}

		public void setDictA(String dictA) {
			this.dictA = dictA;
		}
		public DemoEntity() {
			// TODO Auto-generated constructor stub
		}
	}