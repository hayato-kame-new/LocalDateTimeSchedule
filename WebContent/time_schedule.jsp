<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>タイムスケジュール登録</title>

<style>
table.sche{border:1px solid #a9a9a9;padding:0px;margin:0px;border-collapse:collapse;}
td{vertical-align:top;margin:0px;padding:2px;font-size:0.75em;height:20px;}
td.top{border-bottom:1px solid #a9a9a9;text-align:center;}
td.time{background-color:#f0f8ff;text-align:right;border-right:1px double #a9a9a9;padding-right:5px;}
td.timeb{background-color:#f0f8ff;border-bottom:1px solid #a9a9a9;border-right:1px double #a9a9a9;}
td.contents{background-color:#ffffff;border-bottom:1px dotted #a9a9a9;}
td.contentsb{background-color:#ffffff;border-bottom:1px solid #a9a9a9;}
td.ex{background-color:#ffebcd;border:1px solid #8b0000;}
img{border:0px;}
p{font-size:0.75em;}

#contents{margin:0;padding:0;width:710px;}
#left{margin:0;padding:0;float:left;width:400px;}
#right{margin:0;padding:0;float:right;width:300px;background-color:#ffffff;}
#contents:after{content:".";display:block;height:0;clear:both;visibility:hidden;}
</style>
</head>
<body>
<h3>タイムスケジュール登録</h3>

<div id="contents">

<div id="left">

<table class="sche">
<tr><td class="top" style="width:80px">����</td><td class="top" style="width:300px">�\��</td></tr>
<tr><td class="time">00:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">01:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">02:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">03:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">04:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">05:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">06:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">07:00</td><td class="ex" rowspan="3"></td></tr>
<tr><td class="timeb"></td></tr>
<tr><td class="time">08:00</td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">09:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">10:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">11:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">12:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">13:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">14:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">15:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">16:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">17:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">18:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">19:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">20:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">21:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">22:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>
<tr><td class="time">23:00</td><td class="contents"></td></tr>
<tr><td class="timeb"></td><td class="contentsb"></td></tr>

</table>

</div>

<div id="right">

<form method="post" action="">
<table>
<tr>
<td nowrap>���t</td><td>
<select name="YEAR">
<option value="2005">2005�N
<option value="2006" selected>2006�N
<option value="2007">2007�N
<option value="2008">2008�N
<option value="2009">2009�N
<option value="2010">2010�N
</select>
<select name="MONTH">
<option value="1" selected>1��
<option value="2">2��
<option value="3">3��
<option value="4">4��
<option value="5">5��
<option value="6">6��
<option value="7">7��
<option value="8">8��
<option value="9">9��
<option value="10">10��
<option value="11">11��
<option value="12">12��
</select>
<select name="DAY">
<option value="1">1��
<option value="2">2��
<option value="3">3��
<option value="4">4��
<option value="5">5��
<option value="6">6��
<option value="7">7��
<option value="8">8��
<option value="9">9��
<option value="10">10��
<option value="11">11��
<option value="12">12��
<option value="13">13��
<option value="14">14��
<option value="15">15��
<option value="16">16��
<option value="17">17��
<option value="18">18��
<option value="19">19��
<option value="20">20��
<option value="21">21��
<option value="22">22��
<option value="23" selected>23��
<option value="24">24��
<option value="25">25��
<option value="26">26��
<option value="27">27��
<option value="28">28��
<option value="29">29��
<option value="30">30��
<option value="31">31��
</select>
</td></tr>
<tr><td nowrap>����</td><td>
<select name="SHOUR">
<option value="0">0��
<option value="1">1��
<option value="2">2��
<option value="3">3��
<option value="4">4��
<option value="5">5��
<option value="6">6��
<option value="7">7��
<option value="" selected>--��
<option value="8">8��
<option value="9">9��
<option value="10">10��
<option value="11">11��
<option value="12">12��
<option value="13">13��
<option value="14">14��
<option value="15">15��
<option value="16">16��
<option value="17">17��
<option value="18">18��
<option value="19">19��
<option value="20">20��
<option value="21">21��
<option value="22">22��
<option value="23">23��
</select>
<select name="SMINUTE">
<option value="0">00��
<option value="30">30��
</select>
�`
<select name="EHOUR">
<option value="">--��
<option value="0">0��
<option value="1">1��
<option value="2">2��
<option value="3">3��
<option value="4">4��
<option value="5">5��
<option value="6">6��
<option value="7">7��
<option value="" selected>--��
<option value="8">8��
<option value="9">9��
<option value="10">10��
<option value="11">11��
<option value="12">12��
<option value="13">13��
<option value="14">14��
<option value="15">15��
<option value="16">16��
<option value="17">17��
<option value="18">18��
<option value="19">19��
<option value="20">20��
<option value="21">21��
<option value="22">22��
<option value="23">23��
</select>
<select name="EMINUTE">
<option value="">--��
<option value="0">00��
<option value="30">30��
</select>
</td></tr>

<tr>
<td nowrap>�\��</td>
<td><input type="text" name="PLAN" value="" size="30" maxlength="100">
</td>
</tr>

<tr>
<td valign="top" nowrap>����</td>
<td><textarea name="MEMO" cols="30" rows="10" wrap="virtual"></textarea></td>
</tr>
</table>

<p>
<input type="submit" name="Register" value="�o�^����"> <input type="reset" value="���͂�����">
<p>
</form>

</div>

</div>
</body>
</html>