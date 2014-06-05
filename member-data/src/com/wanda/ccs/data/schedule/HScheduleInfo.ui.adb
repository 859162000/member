<?xml version='1.0' encoding='UTF-8'?>

<adb>
	<uiwrapper name="info" class="com.wanda.ccs.schedule.HScheduleInfoUI" />
	<property name="seqId" id="seqId" />
	<property name="month" id="month">
		<label lsid="CN">年月</label>
		<label lsid="EN">Year/Month</label>
		<out>
			<deco type="format">yyyy-MM</deco>
		</out>
		<in>
			<text width="6" maxlength="10" />
		</in>
	</property>
	<property name="status" id="status" wrap="info" wrapname="status">
		<label lsid="CN">状态</label>
		<label lsid="EN">Status</label>
		<in>
			<select />
		</in>
	</property>
	<property name="version" id="version" wrap="info" wrapname="version">
		<label>版本</label>
		<in>
			<select />
		</in>
	</property>
	<property name="updateTime" id="submitTime">
		<label>更新时间</label>
		<out>
			<deco type="format">yyyy-MM-dd HH:mm:ss</deco>
		</out>
	</property>
	<property name="updateBy" id="submitBy">
		<label>更新人</label>
	</property>
	<property name="submitTime" id="submitTime">
		<label>提交时间</label>
		<out>
			<deco type="format">yyyy-MM-dd HH:mm</deco>
		</out>
	</property>
	<property name="submitBy" id="submitBy">
		<label>提交人</label>
	</property>
	<property name="approvedTime" id="approvedTime">
		<label>审批时间</label>
		<out>
			<deco type="format">yyyy-MM-dd HH:mm</deco>
		</out>
	</property>

	<property name="approvedBy" id="approvedBy">
		<label>审批人</label>
	</property>
	<group id="search">
		<propref name="month" />
		<propref name="status" />
		<propref name="version" />
	</group>

	<group id="list">
		<propref name="month" sortasc="0" sortdesc="1" />
		<propref name="status" sortasc="2" sortdesc="3" />
		<propref name="version" sortasc="4" sortdesc="5" />
		<propref name="updateTime" sortasc="6" sortdesc="7" />
		<propref name="updateBy" sortasc="8" sortdesc="9" />
		<propref name="submitTime" sortasc="6" sortdesc="7" />
		<propref name="submitBy" sortasc="8" sortdesc="9" />
		<propref name="approvedTime" sortasc="10" sortdesc="11" />
		<propref name="approvedBy" sortasc="12" sortdesc="13" />
	</group>

</adb>
