package fr.dila.solonepg.webengine.wsutil;

import java.util.List;

import javax.mail.Address;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.user.STUser;

public class MockUserService implements STUserService {

	private static final long	serialVersionUID	= 1L;

	@Override
	public String getUserFullNameAndCivilite(String userLogin) {

		return null;
	}

	@Override
	public String getUserInfo(String userLogin, String format) {

		return null;
	}

	@Override
	public String getUserFullNameAndCivilite(DocumentModel userModel) throws ClientException {

		return null;
	}

	@Override
	public String getUserInfo(DocumentModel userModel, String format) throws ClientException {

		return null;
	}

	@Override
	public String getUserFullName(String userLogin) {
		return "ADMIN-SOLON ADMIN-SOLON";
	}

	@Override
	public String getUserFullName(DocumentModel userModel) throws ClientException {

		return null;
	}

	@Override
	public String getUserProfils(String userId) throws ClientException {

		return null;
	}

	@Override
	public String generateAndSaveNewUserPassword(String userId) throws ClientException {

		return null;
	}

	@Override
	public boolean isUserPasswordResetNeeded(String userLogin) throws ClientException {

		return false;
	}

	@Override
	public void askResetPassword(String userLogin, String email) throws Exception {

	}

	@Override
	public List<Address> getAddressFromUserList(List<STUser> recipients) throws ClientException {

		return null;
	}

	@Override
	public List<String> getEmailAddressFromUserList(List<STUser> recipients) throws ClientException {

		return null;
	}

	@Override
	public void forceChangeOutdatedPassword(String userLogin) throws ClientException {

	}

	@Override
	public String getUserMinisteres(String userId) throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllDirectionsRattachement(String userId) throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllUserMinisteresId(String userId) throws ClientException {
		return null;
	}

	@Override
	public String getUserPostes(String userId) throws ClientException {
		return null;
	}

}
