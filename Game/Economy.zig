const std = @import("std");
const Account = @import("Account.zig").Account;

pub const TestPlayer = struct {
    account: Account,

    pub fn initMax() TestPlayer {
        return .{ .account = Account.maxTest() };
    }

    pub fn resetMax(self: *TestPlayer) void {
        self.account.money = Account.max_money;
        self.account.diamonds = Account.max_diamonds;
    }

    pub fn save(self: *const TestPlayer) !void {
        try std.fs.cwd().makePath("Game/data");
        var file = try std.fs.cwd().createFile("Game/data/player.txt", .{ .truncate = true });
        defer file.close();

        var buf: [256]u8 = undefined;
        const text = try std.fmt.bufPrint(&buf,
            "id={d}\nmoney={d}\ndiamonds={d}\nwheat={d}\n",
            .{ self.account.id, self.account.money, self.account.diamonds, self.account.wheat },
        );
        try file.writeAll(text);
    }

    pub fn debugPrint(self: *const TestPlayer) void {
        std.debug.print("[LOCAL TEST] id={d} money={d} diamonds={d} wheat={d}\n", .{
            self.account.id,
            self.account.money,
            self.account.diamonds,
            self.account.wheat,
        });
    }
};

pub fn createMaxPlayer() TestPlayer {
    return TestPlayer.initMax();
}
