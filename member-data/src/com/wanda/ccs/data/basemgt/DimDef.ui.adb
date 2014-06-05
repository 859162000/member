<?xml version='1.0' encoding='UTF-8'?>

<adb>
	<uiwrapper name="def" />
	<property name="seqId" id="sid" />

	<property name="searchTypeId" wrap="def" wrapname="typeId" id="typeId">
		<label lsid="CN">查询类型</label>
		<label lsid="EN">TypeId</label>
		<in>
			<select />
		</in>
	</property>
	
	<property name="searchIsDelete" wrap="def" wrapname="isDelete" id="isDelete">
		<label lsid="CN">是否已删除</label>
		<label lsid="EN">IsDelete</label>
		<in>
			<radio />
		</in>
	</property>

	<property name="searchCode" get="getCode" set="setSearchCode"
		id="code">
		<label lsid="CN">编码</label>
		<label lsid="EN">Code</label>

		<in>
			<text width="10" maxlength="20" />
			<!--
			<validator skip="next" method="checkSearchCode">
				<msg>输入编码不能包含特殊字符。</msg>
			</validator>
		-->
		</in>
	</property>

	<property name="searchName" get="getName" set="setSearchName"
		id="name">
		<label lsid="CN">名称</label>
		<label lsid="EN">Name</label>

		<in>
			<text width="10" maxlength="20" />
		<!--	
			<validator skip="next" method="checkSearchName">
				<msg>输入名称不能包含特殊字符。</msg>
			</validator>
		-->
		</in>
	</property>

	<property name="code" id="code" mandatory="yes">
		<label>编码</label>

		<out>
			<align>center</align>
		</out>

		<in>
			<text width="10" maxlength="20" />

			<validator id="ne" skip="next">
				<msg>请输入编码。</msg>
			</validator>
			<validator skip="next" method="checkCode">
				<msg>该编码已经存在!</msg>
			</validator>
			<validator skip="next" method="checkCodeLenth">
				<msg>编码长度不能超过20个字节!</msg>
			</validator>
		</in>
	</property>

	<property name="name" id="name" mandatory="yes">
		<label>名称</label>

		<out>
			<align>center</align>
		</out>

		<in>
			<text width="10" maxlength="20" />

			<validator id="ne" skip="next">
				<msg>请输入名称。</msg>
			</validator>
			<validator skip="next" method="checkNameLenth">
				<msg>名称长度不能超过20个字节!</msg>
			</validator>
		</in>
	</property>
	<property name="typeId" id="typeId" mandatory="yes">
		<label>类型名称</label>

		<out>
			<align>center</align>
		</out>

		<in>
			<text width="10" maxlength="10" />

		</in>
	</property>

	<group id="search">
		<propref name="searchTypeId" />
		<propref name="searchIsDelete" />
		<propref name="searchCode" />
		<propref name="searchName" />
	</group>

	<group id="list">
		<propref name="code" sortasc="0" sortdesc="1" />
		<propref name="name"  />
		<propref name="searchTypeId" />
	</group>

	<group id="edit">
		<propref name="code" />
		<propref name="name" />
	</group>
</adb>
