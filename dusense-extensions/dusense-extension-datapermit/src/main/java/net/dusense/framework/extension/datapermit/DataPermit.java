package net.dusense.framework.extension.datapermit;

import lombok.Data;

/**
 * namespace:object#relation@subject videos:cat.mp4#view@felix
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/09/23
 */
@Data
public class DataPermit {

    private String namespace;
    private String object;
    private String relation;
    private String subject;

    /**
     * <relation-tuple> ::= <object>'#'relation'@'<subject> <object> ::= namespace':'object_id
     * <subject> ::= subject_id | <subject_set> <subject_set> ::= <object>'#'relation
     *
     * <p>namespace:object#relation@subject
     *
     * @return
     */
    public String relationTuple() {
        /**
         * groups:admin#member@felix groups:admin#member@john
         * videos:cat.mp4#view@(groups:admin#member)
         */

        /**
         * // user1 has access on dir1 dir1#access@user1 // Have a look on the subjects concept page
         * if you don't know the empty relation. dir1#parent@(file1#) // Everyone with access to
         * dir1 has access to file1. This would probably be defined // through a subject set rewrite
         * that defines this inherited relation globally. // In this example, we define this tuple
         * explicitly. file1#access@(dir1#access) // Direct access on file2 was granted.
         * file2#access@user1 // user2 is owner of file2 file2#owner@user2 // Owners of file2 have
         * access to it; possibly defined through subject set rewrites. file2#access@(file2#owner)
         */

        /**
         * videos /cats owner cat lady videos /cats view videos:/cats#owner videos /cats/1.mp4 owner
         * videos:/cats#owner videos /cats/1.mp4 view * videos /cats/1.mp4 view
         * videos:/cats/1.mp4#owner videos /cats/2.mp4 owner videos:/cats#owner videos /cats/2.mp4
         * view videos:/cats/2.mp4#owner
         */
        return String.format("%:%#%@%", namespace, object, relation, subject);
    }
}
