<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actFow" value="${ForwardConst.ACT_FOW.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />


<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>フォロワー 一覧</h2>
        <table id="follower_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th class="report_action">詳細</th>
                </tr>
                <c:forEach var="employee" items="${employees}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${employee.code}" /></td>
                        <td ><c:out value="${employee.name}" /></td>
                        <td class="report_action"><a href="<c:url value='?action=${actFow}&command=${commShow}&id=${employee.id}' />">日報一覧を見る</a></td>

                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </c:param>
</c:import>