package com.example.wp4u.data

/**
 * Minimal service locator so every screen shares one repository instance.
 * When the Room layer is ready, this is the ONLY line that changes:
 *   val repository: BoardRepository = RoomBoardRepository(...)
 */
object ServiceLocator {
    val repository: BoardRepository by lazy { FakeBoardRepository() }
}