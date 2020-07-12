package org.shipkit.changelog

import spock.lang.Specification

class ChangelogFormatTest extends Specification {

    def "creates changelog"() {
        def improvements = [
                new Ticket(11, "fixed bug", "https://github.com/myorg/myrepo/pulls/11"),
                new Ticket(12, "improved feature", "https://github.com/myorg/myrepo/issues/12"),
        ]
        when:
        def changelog = ChangelogFormat.formatChangelog(['mockitoguy', 'john'], improvements, 5,
                "1.0.0", "v0.0.9", "https://github.com/myorg/myrepo",
                "2020-01-01")

        then:
        changelog == """<sup><sup>*Changelog generated by [Shipkit Changelog Gradle Plugin](https://github.com/shipkit/org.shipkit.shipkit-changelog)*</sup></sup>

#### 1.0.0
 - 2020-01-01 - [5 commit(s)](https://github.com/myorg/myrepo/compare/v0.0.9...v1.0.0) by mockitoguy, john
 - fixed bug [(#11)](https://github.com/myorg/myrepo/pulls/11)
 - improved feature [(#12)](https://github.com/myorg/myrepo/issues/12)"""
    }

    def "no improvements"() {
        when:
        def changelog = ChangelogFormat.formatChangelog(['mockitoguy'], [], 2,
                "2.0.0", "v1.5.5", "https://github.com/myorg/myrepo",
                "2020-01-01")

        then:
        changelog == """<sup><sup>*Changelog generated by [Shipkit Changelog Gradle Plugin](https://github.com/shipkit/org.shipkit.shipkit-changelog)*</sup></sup>

#### 2.0.0
 - 2020-01-01 - [2 commit(s)](https://github.com/myorg/myrepo/compare/v1.5.5...v2.0.0) by mockitoguy
 - No notable improvements. No pull requests (issues) were referenced from commits."""
    }
}
