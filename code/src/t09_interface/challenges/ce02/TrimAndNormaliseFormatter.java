package t09_interface.challenges.ce02;

/**
 * Trims whitespace from all String fields and lowercases the email.
 */
public class TrimAndNormaliseFormatter implements IFormatter {

    @Override
    public Contact format(Contact input) {
        String id = safeTrim(input.getId());
        String name = safeTrim(input.getName());
        String email = safeTrim(input.getEmail()).toLowerCase();
        String phone = safeTrim(input.getPhone());
        String dept = safeTrim(input.getDepartment());
        String country = safeTrim(input.getCountry());
        String company = safeTrim(input.getCompany());

        return new Contact(
            id,
            name,
            email,
            phone,
            dept,
            country,
            company,
            input.isFollowUp(),
            input.getTurnoverThousands()
        );
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }
}
