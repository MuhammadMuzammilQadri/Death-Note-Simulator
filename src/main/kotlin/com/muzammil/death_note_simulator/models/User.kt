package com.muzammil.death_note_simulator.models

import javax.management.relation.Role

/**
 * Created by Muzammil on 1/9/21.
 */
class User(val userName: String,
           val password: String,
           val isEnabled: Boolean,
           val roles: List<AppRole>) {
}
