package fr.dila.solonepg.webengine.wsutil;

import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.user.STUser;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.mail.Address;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.query.sql.model.OrderByExpr;
import org.nuxeo.ecm.core.query.sql.model.Predicate;

public class MockUserService implements STUserService {
    private static final long serialVersionUID = 1L;

    @Override
    public String getUserFullNameAndCivilite(String userLogin) {
        return null;
    }

    @Override
    public String getUserInfo(String userLogin, String format) {
        return null;
    }

    @Override
    public String getUserFullNameAndCivilite(DocumentModel userModel) {
        return null;
    }

    @Override
    public String getUserInfo(DocumentModel userModel, String format) {
        return null;
    }

    @Override
    public String getUserFullName(String userLogin) {
        return "ADMIN-SOLON ADMIN-SOLON";
    }

    @Override
    public String getUserFullName(DocumentModel userModel) {
        return null;
    }

    @Override
    public String getUserProfils(String userId) {
        return null;
    }

    @Override
    public String generateAndSaveNewUserPassword(String userId) {
        return null;
    }

    @Override
    public String saveNewUserPassword(String userId, String newPassword) {
        return null;
    }

    @Override
    public boolean isUserPasswordResetNeeded(String userLogin) {
        return false;
    }

    @Override
    public void askResetPassword(String userLogin, String email) {}

    @Override
    public List<Address> getAddressFromUserList(List<STUser> recipients) {
        return null;
    }

    @Override
    public List<String> getEmailAddressFromUserList(List<STUser> recipients) {
        return null;
    }

    @Override
    public void forceChangeOutdatedPassword(String userLogin) {}

    @Override
    public String getUserMinisteres(String userId) {
        return null;
    }

    @Override
    public String getAllDirectionsRattachement(String userId) {
        return null;
    }

    @Override
    public List<String> getAllUserMinisteresId(String userId) {
        return null;
    }

    @Override
    public String getUserPostes(String userId) {
        return null;
    }

    @Override
    public Optional<STUser> getOptionalUser(String username) {
        return Optional.empty();
    }

    @Override
    public STUser getUser(String userId) {
        return null;
    }

    @Override
    public void clearUserFromCache(String username) {}

    @Override
    public String getLegacyUserFullName(String userLogin) {
        return null;
    }

    @Override
    public String getUserFullNameWithUsername(String userLogin) {
        return null;
    }

    @Override
    public boolean isMigratedUser(String username) {
        return false;
    }

    @Override
    public String getUserFullNameOrEmpty(String userLogin) {
        return null;
    }

    @Override
    public List<DocumentModel> getActiveUserDocs(Predicate... predicates) {
        return null;
    }

    @Override
    public List<String> getActiveUsernames(Predicate... predicates) {
        return null;
    }

    @Override
    public List<STUser> getActiveUsers(Predicate... predicates) {
        return null;
    }

    @Override
    public List<STUser> getActiveUsers(List<OrderByExpr> orderExp, Predicate... predicates) {
        return null;
    }

    @Override
    public List<DocumentModel> getActiveUserDocs(List<OrderByExpr> orderExp, Predicate... predicates) {
        return null;
    }

    @Override
    public List<String> getActiveUsernames(List<OrderByExpr> orderExp, Predicate... predicates) {
        return null;
    }

    @Override
    public Map<String, Calendar> getMapUsernameDateDerniereConnexion(List<DocumentModel> usersDocs) {
        return null;
    }
}
