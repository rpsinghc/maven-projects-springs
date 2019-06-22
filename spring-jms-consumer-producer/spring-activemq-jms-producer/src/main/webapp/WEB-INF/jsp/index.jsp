<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<html lang="en">
<head>
	<link href="<c:url value="/css/myCss.css" />" rel="stylesheet">

</head>
<body>
    	<div id="vendor">
            <h2>Vendor Management</h2>
         </div>

         <div id="vendor">
        		
   					        
   					 <form:form  action="/vendor" method="post" modelAttribute="vendor">
   					          <div class="message">
   					          	<c:if test="${message != null} }">
   					          		<c:out value="{message}"/>
   					          	</c:if>
   					          </div>
   			     		<fieldset>
   			      				<legend>Vendor Information</legend>
   			      				<div>  
			   						<label for="vendorName">Vendor Name</label>
			   						<form:input path="vendorName" />
		   						</div>
   			      				<div>  
			   						<label for="firstName">First Name</label>
			   						<form:input path="firstName" />
		   						</div>
   			      				<div>  
			   						<label for="lastName">Last Name</label>
			   						<form:input path="lastName" />
		   						</div>
   			      				<div>  
			   						<label for="address"> Address</label>
			   						<form:input path="address" />
		   						</div>
   			      				<div>  
			   						<label for="city">City</label>
			   						<form:input path="city" />
		   						</div>
   			      				<div>  
			   						<label for="state">State</label>
			   						<form:select path="state">
			   							<form:option value="Karnataka">Karnataka</form:option>
			   							<form:option value="Uttar Pradesh">Uttar Pradesh</form:option>
			   						</form:select>
		   						</div>
   			      				<div>  
			   						<label for="zip">Zip</label>
			   						<form:input path="zip" />
		   						</div>
   			      				<div>  
			   						<label for="email">Email</label>
			   						<form:input path="email" />
		   						</div>
   			      				<div>  
			   						<label for="phoneNumber">Phone Number</label>
			   						<form:input path="phoneNumber" />
		   						</div>
   			      				<div>  
			   						<input type="submit" value="Submit"/>
		   						</div>
		   						
		   						
						</fieldset>
					 </form:form>  
    </div>
</body>
