const std = @import("std");

pub const Account = struct {
    id: u64 = 1,
    money: u64 = max_money,
    diamonds: u64 = max_diamonds,
    wheat: u64 = 0,

    pub const max_money: u64 = 2_147_483_647;
    pub const max_diamonds: u64 = 2_147_483_647;

    pub fn maxTest() Account {
        return .{};
    }

    pub fn save(self: *const Account, io: std.Io) !void {
        const dir = std.Io.Dir.cwd();
        try dir.createDirPath(io, "Game/data");
        const file = try dir.createFile(io, "Game/data/player.txt", .{ .truncate = true });
        defer file.close(io);

        var buf: [256]u8 = undefined;
        const text = try std.fmt.bufPrint(&buf,
            "id={d}\nmoney={d}\ndiamonds={d}\nwheat={d}\n",
            .{ self.id, self.money, self.diamonds, self.wheat },
        );
        try file.writeStreamingAll(io, text);
    }
};
