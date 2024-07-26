//@formatter:off
/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (https://www.swig.org).
 * Version 4.2.1
 *
 * Do not make changes to this file unless you know what you are doing - modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package me.him188.ani.app.torrent.anitorrent.binding;

public class anitorrentJNI {
  public final static native void torrent_file_t_index_set(long jarg1, torrent_file_t jarg1_, long jarg2);
  public final static native long torrent_file_t_index_get(long jarg1, torrent_file_t jarg1_);
  public final static native void torrent_file_t_name_set(long jarg1, torrent_file_t jarg1_, String jarg2);
  public final static native String torrent_file_t_name_get(long jarg1, torrent_file_t jarg1_);
  public final static native void torrent_file_t_path_set(long jarg1, torrent_file_t jarg1_, String jarg2);
  public final static native String torrent_file_t_path_get(long jarg1, torrent_file_t jarg1_);
  public final static native void torrent_file_t_offset_set(long jarg1, torrent_file_t jarg1_, long jarg2);
  public final static native long torrent_file_t_offset_get(long jarg1, torrent_file_t jarg1_);
  public final static native void torrent_file_t_size_set(long jarg1, torrent_file_t jarg1_, long jarg2);
  public final static native long torrent_file_t_size_get(long jarg1, torrent_file_t jarg1_);
  public final static native long new_torrent_file_t();
  public final static native void delete_torrent_file_t(long jarg1);
  public final static native long new_torrent_info_t();
  public final static native void torrent_info_t_total_size_set(long jarg1, torrent_info_t jarg1_, long jarg2);
  public final static native long torrent_info_t_total_size_get(long jarg1, torrent_info_t jarg1_);
  public final static native void torrent_info_t_num_pieces_set(long jarg1, torrent_info_t jarg1_, int jarg2);
  public final static native int torrent_info_t_num_pieces_get(long jarg1, torrent_info_t jarg1_);
  public final static native void torrent_info_t_piece_length_set(long jarg1, torrent_info_t jarg1_, int jarg2);
  public final static native int torrent_info_t_piece_length_get(long jarg1, torrent_info_t jarg1_);
  public final static native void torrent_info_t_last_piece_size_set(long jarg1, torrent_info_t jarg1_, int jarg2);
  public final static native int torrent_info_t_last_piece_size_get(long jarg1, torrent_info_t jarg1_);
  public final static native void torrent_info_t_files_set(long jarg1, torrent_info_t jarg1_, long jarg2);
  public final static native long torrent_info_t_files_get(long jarg1, torrent_info_t jarg1_);
  public final static native long torrent_info_t_file_count(long jarg1, torrent_info_t jarg1_);
  public final static native long torrent_info_t_file_at(long jarg1, torrent_info_t jarg1_, int jarg2);
  public final static native void delete_torrent_info_t(long jarg1);
  public final static native void torrent_add_info_t_magnet_uri_set(long jarg1, torrent_add_info_t jarg1_, String jarg2);
  public final static native String torrent_add_info_t_magnet_uri_get(long jarg1, torrent_add_info_t jarg1_);
  public final static native void torrent_add_info_t_torrent_file_path_set(long jarg1, torrent_add_info_t jarg1_, String jarg2);
  public final static native String torrent_add_info_t_torrent_file_path_get(long jarg1, torrent_add_info_t jarg1_);
  public final static native void torrent_add_info_t_resume_data_path_set(long jarg1, torrent_add_info_t jarg1_, String jarg2);
  public final static native String torrent_add_info_t_resume_data_path_get(long jarg1, torrent_add_info_t jarg1_);
  public final static native void torrent_add_info_t_kind_set(long jarg1, torrent_add_info_t jarg1_, int jarg2);
  public final static native int torrent_add_info_t_kind_get(long jarg1, torrent_add_info_t jarg1_);
  public final static native int torrent_add_info_t_kKindUnset_get();
  public final static native int torrent_add_info_t_kKindMagnetUri_get();
  public final static native int torrent_add_info_t_kKindTorrentFile_get();
  public final static native long new_torrent_add_info_t();
  public final static native void delete_torrent_add_info_t(long jarg1);
  public final static native void torrent_handle_t_id_set(long jarg1, torrent_handle_t jarg1_, long jarg2);
  public final static native long torrent_handle_t_id_get(long jarg1, torrent_handle_t jarg1_);
  public final static native long torrent_handle_t_get_info_view(long jarg1, torrent_handle_t jarg1_);
  public final static native int torrent_handle_t_kReloadFileSuccess_get();
  public final static native int torrent_handle_t_reload_file(long jarg1, torrent_handle_t jarg1_);
  public final static native boolean torrent_handle_t_is_valid(long jarg1, torrent_handle_t jarg1_);
  public final static native void torrent_handle_t_post_status_updates(long jarg1, torrent_handle_t jarg1_);
  public final static native void torrent_handle_t_post_save_resume(long jarg1, torrent_handle_t jarg1_);
  public final static native void torrent_handle_t_post_file_progress(long jarg1, torrent_handle_t jarg1_);
  public final static native void torrent_handle_t_set_piece_deadline(long jarg1, torrent_handle_t jarg1_, int jarg2, int jarg3);
  public final static native void torrent_handle_t_reset_piece_deadline(long jarg1, torrent_handle_t jarg1_, int jarg2);
  public final static native void torrent_handle_t_clear_piece_deadlines(long jarg1, torrent_handle_t jarg1_);
  public final static native void torrent_handle_t_set_peer_endgame(long jarg1, torrent_handle_t jarg1_, boolean jarg2);
  public final static native void torrent_handle_t_add_tracker(long jarg1, torrent_handle_t jarg1_, String jarg2, short jarg3, short jarg4);
  public final static native void torrent_handle_t_resume(long jarg1, torrent_handle_t jarg1_);
  public final static native void torrent_handle_t_ignore_all_files(long jarg1, torrent_handle_t jarg1_);
  public final static native void torrent_handle_t_set_file_priority(long jarg1, torrent_handle_t jarg1_, int jarg2, short jarg3);
  public final static native long new_torrent_handle_t();
  public final static native void delete_torrent_handle_t(long jarg1);
  public final static native void torrent_stats_t_download_payload_rate_set(long jarg1, torrent_stats_t jarg1_, int jarg2);
  public final static native int torrent_stats_t_download_payload_rate_get(long jarg1, torrent_stats_t jarg1_);
  public final static native void torrent_stats_t_upload_payload_rate_set(long jarg1, torrent_stats_t jarg1_, int jarg2);
  public final static native int torrent_stats_t_upload_payload_rate_get(long jarg1, torrent_stats_t jarg1_);
  public final static native void torrent_stats_t_total_payload_download_set(long jarg1, torrent_stats_t jarg1_, long jarg2);
  public final static native long torrent_stats_t_total_payload_download_get(long jarg1, torrent_stats_t jarg1_);
  public final static native void torrent_stats_t_total_payload_upload_set(long jarg1, torrent_stats_t jarg1_, long jarg2);
  public final static native long torrent_stats_t_total_payload_upload_get(long jarg1, torrent_stats_t jarg1_);
  public final static native void torrent_stats_t_progress_set(long jarg1, torrent_stats_t jarg1_, float jarg2);
  public final static native float torrent_stats_t_progress_get(long jarg1, torrent_stats_t jarg1_);
  public final static native long new_torrent_stats_t();
  public final static native void delete_torrent_stats_t(long jarg1);
  public final static native void session_stats_t_download_payload_rate_set(long jarg1, session_stats_t jarg1_, int jarg2);
  public final static native int session_stats_t_download_payload_rate_get(long jarg1, session_stats_t jarg1_);
  public final static native void session_stats_t_total_uploaded_bytes_set(long jarg1, session_stats_t jarg1_, int jarg2);
  public final static native int session_stats_t_total_uploaded_bytes_get(long jarg1, session_stats_t jarg1_);
  public final static native void session_stats_t_upload_payload_rate_set(long jarg1, session_stats_t jarg1_, int jarg2);
  public final static native int session_stats_t_upload_payload_rate_get(long jarg1, session_stats_t jarg1_);
  public final static native void session_stats_t_total_downloaded_bytes_set(long jarg1, session_stats_t jarg1_, int jarg2);
  public final static native int session_stats_t_total_downloaded_bytes_get(long jarg1, session_stats_t jarg1_);
  public final static native long new_session_stats_t();
  public final static native void delete_session_stats_t(long jarg1);
  public final static native void torrent_resume_data_t_save_to_file(long jarg1, torrent_resume_data_t jarg1_, String jarg2);
  public final static native void call_listener(long jarg1, long jarg2, long jarg3, event_listener_t jarg3_);
  public final static native long new_torrent_resume_data_t();
  public final static native void delete_torrent_resume_data_t(long jarg1);
  public final static native void delete_event_listener_t(long jarg1);
  public final static native void event_listener_t_on_checked(long jarg1, event_listener_t jarg1_, long jarg2);
  public final static native void event_listener_t_on_checkedSwigExplicitevent_listener_t(long jarg1, event_listener_t jarg1_, long jarg2);
  public final static native void event_listener_t_on_torrent_added(long jarg1, event_listener_t jarg1_, long jarg2);
  public final static native void event_listener_t_on_torrent_addedSwigExplicitevent_listener_t(long jarg1, event_listener_t jarg1_, long jarg2);
  public final static native void event_listener_t_on_save_resume_data(long jarg1, event_listener_t jarg1_, long jarg2, long jarg3, torrent_resume_data_t jarg3_);
  public final static native void event_listener_t_on_save_resume_dataSwigExplicitevent_listener_t(long jarg1, event_listener_t jarg1_, long jarg2, long jarg3, torrent_resume_data_t jarg3_);
  public final static native void event_listener_t_on_torrent_state_changed(long jarg1, event_listener_t jarg1_, long jarg2, int jarg3);
  public final static native void event_listener_t_on_torrent_state_changedSwigExplicitevent_listener_t(long jarg1, event_listener_t jarg1_, long jarg2, int jarg3);
  public final static native void event_listener_t_on_block_downloading(long jarg1, event_listener_t jarg1_, long jarg2, int jarg3, int jarg4);
  public final static native void event_listener_t_on_block_downloadingSwigExplicitevent_listener_t(long jarg1, event_listener_t jarg1_, long jarg2, int jarg3, int jarg4);
  public final static native void event_listener_t_on_piece_finished(long jarg1, event_listener_t jarg1_, long jarg2, int jarg3);
  public final static native void event_listener_t_on_piece_finishedSwigExplicitevent_listener_t(long jarg1, event_listener_t jarg1_, long jarg2, int jarg3);
  public final static native void event_listener_t_on_status_update(long jarg1, event_listener_t jarg1_, long jarg2, long jarg3, torrent_stats_t jarg3_);
  public final static native void event_listener_t_on_status_updateSwigExplicitevent_listener_t(long jarg1, event_listener_t jarg1_, long jarg2, long jarg3, torrent_stats_t jarg3_);
  public final static native void event_listener_t_on_file_completed(long jarg1, event_listener_t jarg1_, long jarg2, int jarg3);
  public final static native void event_listener_t_on_file_completedSwigExplicitevent_listener_t(long jarg1, event_listener_t jarg1_, long jarg2, int jarg3);
  public final static native void event_listener_t_on_session_stats(long jarg1, event_listener_t jarg1_, long jarg2, long jarg3, session_stats_t jarg3_);
  public final static native void event_listener_t_on_session_statsSwigExplicitevent_listener_t(long jarg1, event_listener_t jarg1_, long jarg2, long jarg3, session_stats_t jarg3_);
  public final static native long new_event_listener_t();
  public final static native void event_listener_t_director_connect(event_listener_t obj, long cptr, boolean mem_own, boolean weak_global);
  public final static native void event_listener_t_change_ownership(event_listener_t obj, long cptr, boolean take_or_release);
  public final static native void session_settings_t_download_rate_limit_set(long jarg1, session_settings_t jarg1_, int jarg2);
  public final static native int session_settings_t_download_rate_limit_get(long jarg1, session_settings_t jarg1_);
  public final static native void session_settings_t_upload_rate_limit_set(long jarg1, session_settings_t jarg1_, int jarg2);
  public final static native int session_settings_t_upload_rate_limit_get(long jarg1, session_settings_t jarg1_);
  public final static native void session_settings_t_active_seeds_set(long jarg1, session_settings_t jarg1_, int jarg2);
  public final static native int session_settings_t_active_seeds_get(long jarg1, session_settings_t jarg1_);
  public final static native void session_settings_t_active_downloads_set(long jarg1, session_settings_t jarg1_, int jarg2);
  public final static native int session_settings_t_active_downloads_get(long jarg1, session_settings_t jarg1_);
  public final static native void session_settings_t_user_agent_set(long jarg1, session_settings_t jarg1_, String jarg2);
  public final static native String session_settings_t_user_agent_get(long jarg1, session_settings_t jarg1_);
  public final static native void session_settings_t_peer_fingerprint_set(long jarg1, session_settings_t jarg1_, String jarg2);
  public final static native String session_settings_t_peer_fingerprint_get(long jarg1, session_settings_t jarg1_);
  public final static native void session_settings_t_dht_bootstrap_nodes_extra_set(long jarg1, session_settings_t jarg1_, long jarg2);
  public final static native long session_settings_t_dht_bootstrap_nodes_extra_get(long jarg1, session_settings_t jarg1_);
  public final static native void session_settings_t_dht_bootstrap_nodes_extra_add(long jarg1, session_settings_t jarg1_, String jarg2);
  public final static native void session_settings_t_trackers_extra_set(long jarg1, session_settings_t jarg1_, String jarg2);
  public final static native String session_settings_t_trackers_extra_get(long jarg1, session_settings_t jarg1_);
  public final static native void session_settings_t_connections_limit_set(long jarg1, session_settings_t jarg1_, int jarg2);
  public final static native int session_settings_t_connections_limit_get(long jarg1, session_settings_t jarg1_);
  public final static native void session_settings_t_max_peerlist_size_set(long jarg1, session_settings_t jarg1_, int jarg2);
  public final static native int session_settings_t_max_peerlist_size_get(long jarg1, session_settings_t jarg1_);
  public final static native void session_settings_t_handshake_client_version_set(long jarg1, session_settings_t jarg1_, String jarg2);
  public final static native String session_settings_t_handshake_client_version_get(long jarg1, session_settings_t jarg1_);
  public final static native long new_session_settings_t();
  public final static native void delete_session_settings_t(long jarg1);
  public final static native void delete_new_event_listener_t(long jarg1);
  public final static native void new_event_listener_t_on_new_events(long jarg1, new_event_listener_t jarg1_);
  public final static native void new_event_listener_t_on_new_eventsSwigExplicitnew_event_listener_t(long jarg1, new_event_listener_t jarg1_);
  public final static native long new_new_event_listener_t();
  public final static native void new_event_listener_t_director_connect(new_event_listener_t obj, long cptr, boolean mem_own, boolean weak_global);
  public final static native void new_event_listener_t_change_ownership(new_event_listener_t obj, long cptr, boolean take_or_release);
  public final static native void session_t_start(long jarg1, session_t jarg1_, long jarg2, session_settings_t jarg2_);
  public final static native void session_t_resume(long jarg1, session_t jarg1_);
  public final static native String session_t_fetch_magnet(long jarg1, session_t jarg1_, String jarg2, int jarg3, String jarg4);
  public final static native boolean session_t_start_download(long jarg1, session_t jarg1_, long jarg2, torrent_handle_t jarg2_, long jarg3, torrent_add_info_t jarg3_, String jarg4);
  public final static native void session_t_release_handle(long jarg1, session_t jarg1_, long jarg2, torrent_handle_t jarg2_);
  public final static native boolean session_t_set_new_event_listener(long jarg1, session_t jarg1_, long jarg2, new_event_listener_t jarg2_);
  public final static native void session_t_process_events(long jarg1, session_t jarg1_, long jarg2, event_listener_t jarg2_);
  public final static native void session_t_remove_listener(long jarg1, session_t jarg1_);
  public final static native void session_t_wait_for_alert(long jarg1, session_t jarg1_, int jarg2);
  public final static native void session_t_post_session_stats(long jarg1, session_t jarg1_);
  public final static native long new_session_t();
  public final static native void delete_session_t(long jarg1);
  public final static native String lt_version();
  public final static native void install_signal_handlers();

  public static void SwigDirector_event_listener_t_on_checked(event_listener_t jself, long handle_id) {
    jself.on_checked(handle_id);
  }
  public static void SwigDirector_event_listener_t_on_torrent_added(event_listener_t jself, long handle_id) {
    jself.on_torrent_added(handle_id);
  }
  public static void SwigDirector_event_listener_t_on_save_resume_data(event_listener_t jself, long handle_id, long data) {
    jself.on_save_resume_data(handle_id, new torrent_resume_data_t(data, false));
  }
  public static void SwigDirector_event_listener_t_on_torrent_state_changed(event_listener_t jself, long handle_id, int state) {
    jself.on_torrent_state_changed(handle_id, torrent_state_t.swigToEnum(state));
  }
  public static void SwigDirector_event_listener_t_on_block_downloading(event_listener_t jself, long handle_id, int piece_index, int block_index) {
    jself.on_block_downloading(handle_id, piece_index, block_index);
  }
  public static void SwigDirector_event_listener_t_on_piece_finished(event_listener_t jself, long handle_id, int piece_index) {
    jself.on_piece_finished(handle_id, piece_index);
  }
  public static void SwigDirector_event_listener_t_on_status_update(event_listener_t jself, long handle_id, long stats) {
    jself.on_status_update(handle_id, new torrent_stats_t(stats, false));
  }
  public static void SwigDirector_event_listener_t_on_file_completed(event_listener_t jself, long handle_id, int file_index) {
    jself.on_file_completed(handle_id, file_index);
  }
  public static void SwigDirector_event_listener_t_on_session_stats(event_listener_t jself, long handle_id, long stats) {
    jself.on_session_stats(handle_id, new session_stats_t(stats, false));
  }
  public static void SwigDirector_new_event_listener_t_on_new_events(new_event_listener_t jself) {
    jself.on_new_events();
  }

  private final static native void swig_module_init();
  static {
    swig_module_init();
  }
}

//@formatter:on