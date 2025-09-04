# Fishing Event Plugin

A Minecraft Paper plugin that adds fishing events with leaderboards, prizes, and PlaceholderAPI integration.

## Features

- **Fishing Events**: Start timed fishing competitions with configurable duration
- **Leaderboards**: Track player scores with different fish types having different point values
- **Prizes**: Configurable rewards for top 3 players
- **Boss Bar**: Real-time event timer display
- **PlaceholderAPI Integration**: Use placeholders in holograms and other plugins
- **Configurable**: Extensive configuration options for scores, prizes, and messages

## Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/fishevent start <duration>` | `fishingevent.admin` | Start a fishing event (duration in seconds) |
| `/fishstop` | `fishingevent.admin` | Stop the current fishing event |
| `/fishreset` | `fishingevent.admin` | Reset leaderboards |
| `/fishreload` | `fishingevent.admin` | Reload plugin configuration |

## Permissions

- `fishingevent.admin` - Access to all admin commands (default: op)
- `fishingevent.participate` - Can participate in fishing events (default: true)

## PlaceholderAPI Placeholders

Use these placeholders with hologram plugins or any PlaceholderAPI-compatible plugin:

- `%fishingevent_top_name_1%` - Name of 1st place player
- `%fishingevent_top_name_2%` - Name of 2nd place player  
- `%fishingevent_top_name_3%` - Name of 3rd place player
- `%fishingevent_top_score_1%` - Score of 1st place player
- `%fishingevent_top_score_2%` - Score of 2nd place player
- `%fishingevent_top_score_3%` - Score of 3rd place player
- `%fishingevent_player_score%` - Current player's score
- `%fishingevent_active%` - Whether an event is active (true/false)
- `%fishingevent_time_left%` - Remaining time in seconds
- `%fishingevent_participants%` - Number of participating players

## Configuration

The plugin comes with a comprehensive config.yml:

```yaml
# Minimum players required to start an event
min-players: 2

# Enable or disable prize rewards
prizes-enabled: true

# Rewards for winners (executed as console commands)
prizes:
  1: "give %player% diamond 3"
  2: "give %player% gold_ingot 5"
  3: "give %player% iron_ingot 10"

# Scoring per fish type
fish-scores:
  COD: 1
  SALMON: 2
  TROPICAL_FISH: 3
  PUFFERFISH: 5
```

## Installation

1. Download the plugin JAR file
2. Place it in your server's `plugins` folder
3. Install PlaceholderAPI if you haven't already
4. Restart your server
5. Configure the plugin in `plugins/FishingEventPlugin/config.yml`

## Dependencies

- **Required**: Paper 1.21.8+ (or compatible)
- **Required**: PlaceholderAPI

## Building from Source

1. Clone this repository
2. Make sure you have Maven installed
3. Run `mvn clean package`
4. The compiled JAR will be in the `target` folder

## Support

If you encounter any issues or have suggestions, please open an issue on the GitHub repository.

## Note

Gatau bjir gabut aja pgn bikin ginian aokwkkw

---

**Author**: freyza  
**Version**: 1.0.0  
**Minecraft Version**: 1.21.8# fishing-event-plugin
