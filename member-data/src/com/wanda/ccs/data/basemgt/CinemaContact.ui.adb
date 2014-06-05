<?xml version='1.0' encoding='UTF-8'?>

<adb>
	<uiwrapper name="def" />
	<property name="seqId" id="sid" />

	<property name="name" id="name" mandatory="yes">
		<label>姓名</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="25" />

			<validator id="ne" skip="next">
				<msg>请输入姓名。</msg>
			</validator>
		</in>
	</property>
	
	<property name="sex" wrap="def" wrapname="sex" id="sex" mandatory="yes">
		<label>性别</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<radio />
		</in>
	</property>
	
	<property name="duty" wrap="def" wrapname="duty" id="duty" mandatory="yes">
		<label>职责</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<select />
		</in>
	</property>
	
		<property name="cinema_Id" wrap="def" wrapname="cinemaId" id="cinemaId" mandatory="yes">
		<label>影院</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<select />
		</in>
	</property>
	
	<property name="phone" id="phone" mandatory="yes">
		<label>电话</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="25" />

			<validator id="ne" skip="next">
				<msg>请输入电话。</msg>
			</validator>
		</in>
	</property>
	
	<property name="email" id="email" mandatory="yes">
		<label>邮箱</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="25" />

			<validator id="ne" skip="next">
				<msg>请输入邮箱。</msg>
			</validator>
		</in>
	</property>
	
	<property name="address" id="address" mandatory="yes">
		<label>住址</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="25" />

			<validator id="ne" skip="next">
				<msg>请输入住址。</msg>
			</validator>
		</in>
	</property>
	
	<property name="post_Code" id="postCode" mandatory="yes">
		<label>邮编</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="25" />

			<validator id="ne" skip="next">
				<msg>请输入邮编。</msg>
			</validator>
		</in>
	</property>
	
	<property name="isDelete" wrap="def" wrapname="isDelete" id="isDelete" mandatory="yes">
		<label>是否删除</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<radio />
		</in>
	</property>
	
	<property name="mobile" id="mobile" mandatory="yes">
		<label>手机</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="10" maxlength="25" />

			<validator id="ne" skip="next">
				<msg>请输入手机。</msg>
			</validator>
		</in>
	</property>
	<group id="searchContact">
		<propref name="seqId" />
	</group>
	<group id="cinemaContactList">
		<propref name="name" sortasc="0" sortdesc="1" />
		<propref name="sex" sortasc="2" sortdesc="3" />
		<propref name="duty" sortasc="4" sortdesc="5" />
		<propref name="phone" sortasc="6" sortdesc="7" />
		<propref name="mobile" sortasc="8" sortdesc="9" />
		<propref name="email" sortasc="10" sortdesc="11" />
	</group>
	
	<group id="editCinemaContact">
		<propref name="cinema_Id" />
		<propref name="name" />
		<propref name="phone" />
		<propref name="email" />
		<propref name="duty" />
		<propref name="address" />
		<propref name="post_Code" />
		<propref name="isDelete" />
		<propref name="sex" />
		<propref name="mobile" />
	</group>
</adb>
