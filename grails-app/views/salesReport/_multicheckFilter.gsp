          <tr class="prop">
            <td class="name">${label}</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('${name}', this.checked)" id="${name}CheckAll"> <label for="${name}CheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:if test="${label == 'Product'  }">
            			<g:each in="${list}" var="object">
            			<div class="checkbox"><input class="${name}Checkbox" type="checkbox" name="${field}" id="${name}${object.id}" value="${object.id}"> <label for="${name}${object.id}">${object.identifier +' - '+ object.description}</label></div>
            			</g:each>
            		</g:if>
            		<g:else>
            			<g:each in="${list}" var="object">
            			<div class="checkbox"><input class="${name}Checkbox" type="checkbox" name="${field}" id="${name}${object.id}" value="${object.id}"> <label for="${name}${object.id}">${object}</label></div>
            			</g:each>
            		</g:else>
            	</div>
            </td>
          </tr>
