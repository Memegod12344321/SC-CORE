const std = @import("std");

/// Local/offline test economy. This is intentionally separate from the
/// existing protocol implementation until the Hay Day message schema is
/// implemented.
pub const TestPlayer = struct {
    pub const max_money: u64 = 2_147_483_647;
    pub const max_diamonds: u64 = 2_147_483_647;

    money: u64 = max_money;
    diamonds: u64 = max_diamonds;

    pub fn initMax() TestPlayer {
        return .{
            .money = max_money,
            .diamonds = max_diamonds,
        };
    }

    pub fn resetMax(self: *TestPlayer) void {
        self.money = max_money;
        self.diamonds = max_diamonds;
    }

    pub fn debugPrint(self: *const TestPlayer) void {
        std.debug.print("[LOCAL TEST] money={d} diamonds={d}\n", .{
            self.money,
            self.diamonds,
        });
    }
};

pub fn createMaxPlayer() TestPlayer {
    return TestPlayer.initMax();
}
