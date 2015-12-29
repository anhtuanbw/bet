package vn.kms.ngaythobet.domain.validation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.util.FieldUtils;

import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserService;

/**
 * 
 * @author thangpham
 *
 */
public class MonderatorAccessValidator implements ConstraintValidator<ModeratorAccess, Object> {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userService;

    private String message;

    @Override
    public void initialize(ModeratorAccess moderatorAccess) {
        this.message = moderatorAccess.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            User loggedUser = userService.getUserInfo();
            Long groupId = (Long) FieldUtils.getFieldValue(value, "groupId");
            String queryString = String.format("select id from Group where id = :groupId and moderator = :user");

            boolean result = !em.createQuery(queryString)
                                .setParameter("groupId", groupId)
                                .setParameter("user", loggedUser)
                                .getResultList()
                                .isEmpty();
            if (!result) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("groupId")
                    .addConstraintViolation();
            }

            return result;
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError("Cannot get groupId value");
        }
    }
}
