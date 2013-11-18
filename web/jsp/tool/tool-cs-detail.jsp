<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:site>
<jsp:attribute name="headPart">
<meta name="keywords" content="${tool.descriptor.keywords}"/>
<meta name="description" content="${tool.descriptor.description}"/>
<style>
	.detail_head:AFTER{
		content: ".";
		visibility: hidden;
		clear:both;  
  		display:block; 
  		height: 0;
	}
	.comments{
		clear:both;
		float:left;
		margin-top: 10px;
	}
	.tool_container{
		float:left;
		width:100%;
		height:500px;
		border: 1px solid #848877;
		-moz-box-shadow: 5px 5px 2px 2px rgba(68, 68, 68, 0.5);
		-webkit-box-shadow: 5px 5px 2px 2px rgba(68, 68, 68, 0.5);
		box-shadow: 5px 5px 2px 2px rgba(68, 68, 68, 0.5);
	}
</style>
<script>
	$(function(){
		var iframe = $(".tool_container");
		iframe.load(function(){
			var me = $(this);
			var body = me.contents().find("body");
			body.css("margin", "0");
			var children = body.children();
			if(children.length > 0){
				var root = children.first();
				var w = Math.max(root.outerWidth(), 800);
				var h = Math.max(root.outerHeight(), 400)
				me.width(w);
				me.height(h);
			}
		});
	});
</script>
</jsp:attribute>
<jsp:body>
	<div class="screen">
		<div class="detail_head">
			<s:tool_detail toolDescriptor="${tool.descriptor}"/>
		</div>
		<iframe class="tool_container" name="tool_container" src="${root}/file/${toolFile}"></iframe>
		<c:if test="${tool.status == 0}">
			<div>
				<a href="${root}/ctrl/tool/approve?toolId=${tool.descriptor.pluginID}" >允许公开</a>
			</div>
		</c:if>
		<div style="clear:left; padding-top:20px;">
			<c:forEach var="t" items="${similars}">
			<div class="left_float" style="padding-right:20px; padding-bottom:5px;">
				<a id="${t.descriptor.pluginID}" href="${root}/ctrl/tool/detail?toolId=${t.descriptor.pluginID}">
					<c:choose>
						<c:when test="${empty t.descriptor.icon}">
							<div class="tool_logo"><span>${t.descriptor.pluginName}</span></div>
						</c:when>
						<c:otherwise>
							<img height="64" width="64" src="${t.descriptor.displayIcon}" style="border:1px #333333 inset;"/>
						</c:otherwise>
					</c:choose>
				</a>
			</div>
			</c:forEach>
			<div style="font-size: 12px; clear:left;"><a href="${root}/ctrl/tool/list">更多</a></div>
		</div>
		<div class="comments">
			<c:forEach var="c" items="${comments}" varStatus="vs">
				<s:comment comment="${c}" 
					subjectId="${tool.descriptor.pluginID}"
					index="${vs.index + 1}"
					closable="${canDelComment}" >
				</s:comment>
			</c:forEach>
			
			<form method="post" action="${root}/ctrl/tool/comment">
				<textarea name="comment" id="comment" rows="6" cols="60"></textarea>
				<input type="hidden" name="toolId" value="${tool.descriptor.pluginID}">
				<input type="submit" class="btn" value="评论">
			</form>
		</div>
	</div>
</jsp:body>
</s:site>
